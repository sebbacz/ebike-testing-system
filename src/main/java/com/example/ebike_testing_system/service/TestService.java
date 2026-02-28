package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.dto.StartTestDto;
import com.example.ebike_testing_system.dto.TestDto;
import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.repository.TestRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final TestReportService testReportService;
    private final TestbenchService testbenchService;
    private final CustomerService customerService;
    private final EbikeService ebikeService;
    private final AccountService accountService;
    private final AdminService adminService;
    private static final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

    public TestService(TestRepository testRepository, TestReportService testReportService,
                       TestbenchService testbenchService, CustomerService customerService,
                       AdminService adminService,
                       EbikeService ebikeService, AccountService accountService) {
        this.testRepository = testRepository;
        this.testReportService = testReportService;
        this.testbenchService = testbenchService;
        this.customerService = customerService;
        this.ebikeService = ebikeService;
        this.adminService = adminService;
        this.accountService = accountService;
    }

    public Account getLoggedInUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return accountService.findByEmail(username);
    }

    public List<Test> findAll() {
        return testRepository.findAll();
    }

    public Optional<Test> findByUuid(UUID uuid) {
        return testRepository.findByUuid(uuid);
    }

    public List<Test> getTestsForCurrentUser() {
        Account user = getLoggedInUser();
        if (user.getRole() == Roles.ADMIN) {
//            return Collections.emptyList();
            return adminService.getTestByAdminCompany();
        } else if (user.getRole() == Roles.TECHNICIAN) {
            return testRepository.findAll();
        } else {
            return Collections.emptyList();
        }
    }


    public List<Test> findByCustomerId(int id) {
        return testRepository.findByCustomerId(id);
    }

    public Test findById(int id) {
        return testRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException("Test not found"));
    }


    public Test updateTestNotes(int id, TestDto dto) {
        Test test = findById(id); // throws if not found

        test.setNotes(dto.getNotes());
        return testRepository.save(test);
    }


    @Transactional
    public Test createAndLinkTest(StartTestDto dto, boolean isImported) {
        // Fetch related entities
        Ebike ebike = ebikeService.findById(dto.ebikeId());
        Customer customer = customerService.findById(dto.customerId());
        Technician technician = accountService.getTechnicianById(dto.technicianId());

        // Build and save new test
        Test newTest = new Test();
        newTest.setStatus(TestStatus.PENDING);
        newTest.setStartDate(LocalDateTime.now());
        newTest.setType(dto.type());
        newTest.setBatteryCapacity(dto.batteryCapacity() != null ? dto.batteryCapacity() : 0);
        newTest.setMaxSupport(dto.maxSupport() != null ? dto.maxSupport() : 0);
        newTest.setEnginePowerMax(dto.enginePowerMax() != null ? dto.enginePowerMax() : 0);
        newTest.setEnginePowerNominal(dto.enginePowerNominal() != null ? dto.enginePowerNominal() : 0);
        newTest.setEngineTorque(dto.engineTorque() != null ? dto.engineTorque() : 0);
        newTest.setEbike(ebike);
        newTest.setCustomer(customer);
        newTest.setTechnician(technician);

        testRepository.save(newTest);
        if (!isImported) {
            UUID uuid = testbenchService.updateUuidFromBench(newTest);
            if (uuid != null) {
                newTest.setStatus(TestStatus.IN_PROGRESS);
                newTest.setUuid(uuid);
                testRepository.save(newTest);

                // Kick off the async monitoring
                testbenchService.monitorTestLifecycleAsync(uuid, newTest);
            } else {
                newTest.setStatus(TestStatus.FAILED);
                testRepository.save(newTest);
            }
        } else {
            newTest.setUuid(UUID.randomUUID());
            newTest.setStatus(TestStatus.COMPLETED);
            newTest.setEndDate(LocalDateTime.now());
            testRepository.save(newTest);
        }

        return newTest;
    }

    public List<Test> getTestHistoryForBike(int bikeId) {

        return testRepository.findByEbikeId(bikeId);
    }

    public List<Test> findByBikeId(int bikeId) {
        return testRepository.findByEbikeId(bikeId);
    }

    public List<Test> getTestHistoryForToken(String token) {

        int bikeId = mapTokenToBikeId(token);
        return testRepository.findByEbikeId(bikeId);
    }

    private int mapTokenToBikeId(String token) {

        return 1;
    }


}
