package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.exception.TestFailureException;
import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.repository.TestRepository;
import com.example.ebike_testing_system.repository.TestReportRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

@Service
public class TestbenchService {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final TestRepository testRepository;

    private final TestReportService testReportService;
    private final TestReportRepository testReportRepository;

    @Autowired
    private ApplicationContext applicationContext;
    private final CustomerService customerService;
    private final EbikeService ebikeService;
    private final AccountService accountService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestbenchService.class);


    @Value("${testbench.csv-dir:src/main/resources/csv}")
    private String savePath;

    public TestbenchService(TestRepository testRepository,
                            TestReportService testReportService,
                            TestReportRepository testReportRepository,
                            RestTemplateBuilder restTemplateBuilder,
                            @Value("${testbench.api.key}") String apiKey,
                            @Value("${testbench.base.url}") String baseUrl,
                            CustomerService customerService, EbikeService ebikeService, AccountService accountService) {

        this.testRepository = testRepository;
        this.testReportService = testReportService;
        this.testReportRepository = testReportRepository;
        this.customerService = customerService;
        this.ebikeService = ebikeService;
        this.accountService = accountService;
        this.restTemplate = restTemplateBuilder
                .defaultHeader("X-Api-Key", apiKey)
                .build();

        this.baseUrl = baseUrl;
    }

    public List<Test> findAll() {
        return testRepository.findAll();
    }

    public UUID updateUuidFromBench(Test test) {
        String url = baseUrl + "/api/test";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(test, headers),
                    String.class
            );

            if (response.getStatusCode().is2xxSuccessful() && !response.getBody().isEmpty()) {
                return UUID.fromString(new ObjectMapper()
                        .readTree(response.getBody())
                        .path("id") // or "uuid"
                        .asText());
            }
        } catch (Exception e) {
            LOGGER.error("Failed to contact bench API: " + e.getMessage());
        }

        return null;
    }


//    public void downloadAndParseReport(UUID batchId, Test parentTest) {
//        String url = baseUrl + "/api/test/" + batchId + "/report";
//        HttpHeaders headers = new HttpHeaders();
//        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));
//
//        try {
//            ResponseEntity<byte[]> rsp = restTemplate.exchange(
//                    url, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);
//
//            if (!rsp.getStatusCode().is2xxSuccessful() || rsp.getBody() == null) {
//                LOGGER.error("Bench replied with " + rsp.getStatusCode());
//                return;
//            }
//
//            Path dir = Paths.get(savePath);
//            Files.createDirectories(dir);
//            Path file = dir.resolve(batchId + ".csv");
//            Files.write(file, rsp.getBody());
//
//            List<TestReport> rows = testReportService.parseCSVFromFile(file.toString(), batchId);
//            rows.forEach(r -> r.setTest(parentTest));
//            testReportRepository.saveAll(rows);
//
//            LOGGER.info("CSV saved to " + file.toAbsolutePath() +
//                    " and " + rows.size() + " test report rows inserted.");
//        } catch (HttpClientErrorException.Conflict e) {
//            LOGGER.error("Report not ready (409): " + e.getMessage());
//        } catch (IOException e) {
//            throw new RuntimeException("Failed to save or parse CSV", e);
//        }
//    }


    //    @Async
    public void waitUntilTestCompleted(UUID uuid) throws TestFailureException {
        String url = baseUrl + "/api/test/" + uuid;
        int maxAttempts = 100;

        for (int i = 0; i < maxAttempts; i++) {
            try {
                ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    JsonNode root = new ObjectMapper().readTree(response.getBody());
                    String state = root.path("state").asText();

                    if ("COMPLETED".equalsIgnoreCase(state)) {
                        LOGGER.info("Test {} is COMPLETED", uuid);
//                        downloadTestReport(uuid); // Only now it's safe
                        return;
                    } else {
                        LOGGER.info("Test {} not complete yet (state: {}). Attempt {}/{}", uuid, state, i + 1, maxAttempts);
                    }
                } else {
                    LOGGER.warn("Received unexpected response while checking test state: {}", response.getStatusCode());
                }

            } catch (Exception e) {
                LOGGER.error("Polling failed for test {} on attempt {}: {}", uuid, i + 1, e.getMessage());
            }

            artificialWaitingTime(); // non-blocking readable sleep
        }

        throw new TestFailureException("Test " + uuid + " did not complete after " + maxAttempts + " attempts.");
    }


    @Async
    @Transactional
    public void monitorTestLifecycleAsync(UUID uuid, Test test) {

        try {
//            TestbenchService proxy = applicationContext.getBean(TestbenchService.class);
//            proxy.waitUntilTestCompleted(uuid);
            waitUntilTestCompleted(uuid);

            boolean parsed = false;
            for (int i = 0; i < 5; i++) {
                parsed = downloadAndParseReport(uuid, test);
                if (parsed) break;
                artificialWaitingTime();
            }

            if (parsed) {
                test.setStatus(TestStatus.COMPLETED);
                test.setEndDate(LocalDateTime.now());
            } else {
                test.setStatus(TestStatus.FAILED);
                LOGGER.warn("Test {} completed but report parsing failed.", uuid);
            }

        } catch (TestFailureException e) {
            LOGGER.error("Test {} did not complete in time: {}", uuid, e.getMessage());
            test.setStatus(TestStatus.FAILED);
        }

        testRepository.save(test);
    }



    public boolean downloadAndParseReport(UUID batchId, Test parentTest) {
        String url = baseUrl + "/api/test/" + batchId + "/report";
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_OCTET_STREAM));

        try {
            ResponseEntity<byte[]> rsp = restTemplate.exchange(
                    url, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);

            if (!rsp.getStatusCode().is2xxSuccessful() || rsp.getBody() == null) {
                LOGGER.error("Bench replied with {}", rsp.getStatusCode());
                return false;
            }

            Path dir = Paths.get(savePath);
            Files.createDirectories(dir);
            Path file = dir.resolve(batchId + ".csv");
            Files.write(file, rsp.getBody());

            List<TestReport> rows = testReportService.parseCSVFromFile(file.toString(), batchId);
            rows.forEach(r -> r.setTest(parentTest));
            testReportRepository.saveAll(rows);

            LOGGER.info("CSV saved and {} report rows inserted into DB.", rows.size());
            return true;

        } catch (HttpClientErrorException.Conflict e) {
            LOGGER.warn("Report not ready (409): {}", e.getMessage());
            return false;
        } catch (IOException e) {
            LOGGER.error("Failed to save or parse CSV", e);
            return false;
        }
    }



    private static void artificialWaitingTime() {
        try {
            TimeUnit.MILLISECONDS.sleep(ThreadLocalRandom.current().nextInt(500, 1500));
        } catch (InterruptedException e) {
            LOGGER.warn("Artificial wait interrupted", e);
            Thread.currentThread().interrupt(); // preserve interrupt status
        }
    }

    public Optional<String> fetchTestStateFromBench(UUID uuid) {
        String url = baseUrl + "/api/test/" + uuid;

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = new ObjectMapper().readTree(response.getBody());
                String state = root.path("state").asText(null);

                if (state != null) {
                    return Optional.of(state.toUpperCase());
                }
            }

            LOGGER.warn("Unable to retrieve test state for UUID {}: {}", uuid, response.getStatusCode());
        } catch (Exception e) {
            LOGGER.error("Error checking test state for UUID {}: {}", uuid, e.getMessage());
        }

        return Optional.empty();
    }


    public String checkTestStatus(String testId) {
        String url = baseUrl + "/api/test/" + testId;
        return restTemplate.getForObject(url, String.class);
    }

    public byte[] getTestReport(String testId) {
        String url = baseUrl + "/api/test/" + testId + "/report";
        return restTemplate.getForObject(url, byte[].class);
    }
}
