package com.example.ebike_testing_system.controller;
import com.example.ebike_testing_system.model.Test;

import com.example.ebike_testing_system.dto.TestDto;
import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.service.AccountService;
import org.springframework.ui.Model;
import com.example.ebike_testing_system.dto.StartTestDto;
import com.example.ebike_testing_system.repository.EbikeRepository;
import com.example.ebike_testing_system.service.TestbenchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

import java.util.List;

@Controller
public class TestbenchController {

    private static final Logger logger = LoggerFactory.getLogger(TestbenchController.class);

    private final TestbenchService testbenchService;
    private final AccountService accountService;
    private final EbikeRepository ebikeRepository;

    public TestbenchController(TestbenchService testbenchService, AccountService accountService, EbikeRepository ebikeRepository) {
        this.testbenchService = testbenchService;
        this.accountService = accountService;
        this.ebikeRepository = ebikeRepository;
    }


    @GetMapping("/check-test/{testId}")
//    @ResponseBody
    public String checkTestStatus(@PathVariable String testId) {
        return testbenchService.checkTestStatus(testId);
    }

    @GetMapping("/report/{testId}")
//    @ResponseBody
    public byte[] getTestReport(@PathVariable String testId) {
        return testbenchService.getTestReport(testId);
    }

    @GetMapping("/technician/tests")
    @PreAuthorize("hasAnyRole('TECHNICIAN')")
    public String listTests(Model model) {
        List<Test> allTests = testbenchService.findAll();
        model.addAttribute("tests", allTests);
        return "tech/list-tests";
    }


}
