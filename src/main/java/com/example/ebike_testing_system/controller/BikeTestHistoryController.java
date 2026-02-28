package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.service.TestService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BikeTestHistoryController {

    private final TestService testService;

    public BikeTestHistoryController(TestService testService) {
        this.testService = testService;
    }

    @GetMapping("/bikes/test-history-by-id")
    public List<?> getTestHistory(@RequestParam int bikeId, @RequestParam String token) {
        if (!isValidToken(bikeId, token)) {
            throw new SecurityException("Invalid token");
        }
        return testService.getTestHistoryForBike(bikeId);
    }

    private boolean isValidToken(int bikeId, String token) {
        return token.startsWith(Integer.toHexString(bikeId));
    }
}