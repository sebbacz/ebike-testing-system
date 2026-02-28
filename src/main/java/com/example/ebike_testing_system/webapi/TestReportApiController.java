package com.example.ebike_testing_system.webapi;

import com.example.ebike_testing_system.dto.TestReportDto;
import com.example.ebike_testing_system.model.Test;
import com.example.ebike_testing_system.service.TestReportService;
import com.example.ebike_testing_system.service.TestService;
import com.example.ebike_testing_system.service.TestbenchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/report")
public class TestReportApiController {
    private final TestService testService;
    private final TestReportService testReportService;

    public TestReportApiController(TestService testService, TestReportService testReportService) {
        this.testService = testService;
        this.testReportService = testReportService;
    }

    @GetMapping("/{uuid}")
    public ResponseEntity<List<TestReportDto>> getReportRows(@PathVariable UUID uuid) {
        Test test = testService.findByUuid(uuid)
                .orElseThrow(() -> new NotFoundException("Test not found"));
        List<TestReportDto> reports = testReportService.getReportsForTest(test.getId());
        return ResponseEntity.ok(reports);
    }


}
