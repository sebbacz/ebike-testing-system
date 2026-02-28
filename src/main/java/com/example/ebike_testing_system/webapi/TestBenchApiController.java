package com.example.ebike_testing_system.webapi;

import com.example.ebike_testing_system.dto.TestDto;
import com.example.ebike_testing_system.dto.StartTestDto;
import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.repository.TestReportRepository;
import com.example.ebike_testing_system.service.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/test")
public class TestBenchApiController {

    private final TestbenchService testbenchService;
    private final TestService testService;
    private final EbikeService ebikeService;
    private final AccountService accountService;
    private final ModelMapper modelMapper;

    private final TestReportService testReportService;
    private final TestReportRepository testReportRepository;


    public TestBenchApiController(TestbenchService testbenchService, ModelMapper modelMapper,
                                  TestReportService testReportService, TestReportRepository testReportRepository,
                                  TestService testService, EbikeService ebikeService, AccountService accountService) {
        this.testbenchService = testbenchService;
        this.modelMapper = modelMapper;
        this.testReportService = testReportService;
        this.testReportRepository = testReportRepository;
        this.testService = testService;
        this.ebikeService = ebikeService;
        this.accountService = accountService;

    }

    public void saveAll(List<TestReport> rows) {
        testReportRepository.saveAll(rows);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<TestDto> getTestStatus(@PathVariable("id") int id) {
//        final Test test = testbenchService.findById(id);
//
//        return ResponseEntity.ok(modelMapper.map(test, TestDto.class));
//    }


    @PostMapping()
    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
    public ResponseEntity<TestDto> startTest(@RequestBody @Valid StartTestDto dto, Principal principal) {

        // Get authenticated technician
        String email = principal.getName();
        Account technician = accountService.findByEmail(email);
        int technicianId = technician.getId();

        // Inject DTO with server-side technicianId
        StartTestDto injectedDto = new StartTestDto(
                dto.type(),
                dto.batteryCapacity(),
                dto.maxSupport(),
                dto.enginePowerMax(),
                dto.enginePowerNominal(),
                dto.engineTorque(),
                dto.ebikeId(),
                dto.customerId(),
                technicianId, // <- INJECTED
                ""
        );

        Test createdTest = testService.createAndLinkTest(injectedDto, false);

        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(dto, TestDto.class));
    }


    @PreAuthorize("hasAnyRole('TECHNICIAN','ADMIN') and principal.approved == true")
    @PostMapping("/upload")
    public ResponseEntity<String> uploadCSV(@RequestParam("file") MultipartFile file) {
        UUID batchId = UUID.randomUUID(); // Generate a new UUID for this CSV upload
//        UUID batchId = testbenchService.batchUUID; // Generate a new UUID for this CSV upload
        List<TestReport> dataList = testReportService.parseCSVFromMultipart(file, batchId);
        testReportRepository.saveAll(dataList); // Save all rows with the same batch ID
        return ResponseEntity.ok("CSV data saved successfully with batchId: " + batchId);
    }

    @GetMapping("/report/{batchId}")
    public ResponseEntity<List<TestReport>> getBatchData(@PathVariable UUID batchId) {
        List<TestReport> reports = testReportRepository.findByUuid(batchId);
        if (!reports.isEmpty()) {
            return ResponseEntity.ok(reports);
        }

        // Check if the test is complete before trying to fetch report
        Optional<String> maybeState = testbenchService.fetchTestStateFromBench(batchId);
        if (maybeState.isPresent() && "COMPLETED".equalsIgnoreCase(maybeState.get())) {
            boolean parsed = testbenchService.downloadAndParseReport(batchId,
                    testService.findByUuid(batchId)
                            .orElseThrow(() -> new NotFoundException("Test not found for id " + batchId))
            );

            if (parsed) {
                List<TestReport> parsedReports = testReportRepository.findByUuid(batchId);
                return ResponseEntity.ok(parsedReports);
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Collections.emptyList());
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
    }


}
