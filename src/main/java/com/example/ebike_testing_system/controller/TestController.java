package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.model.Test;
import com.example.ebike_testing_system.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class TestController {

    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/bikes/test-history")
    public List<Test> getTestHistory(@RequestParam String token) {

        return testService.getTestHistoryForToken(token);
    }

    @GetMapping("/tests/report")
    public Test getTestReport(@RequestParam int testId) {
        return testService.findById(testId);
    }
}