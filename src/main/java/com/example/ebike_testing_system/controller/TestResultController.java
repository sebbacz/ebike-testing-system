package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.dto.StartTestDto;
import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
public class TestResultController {

    private final TestReportService testReportService;
    private final TestService testService;
    private final CustomerService customerService;
    private final EbikeService ebikeService;
    private final AccountService accountService;

    public TestResultController(TestReportService testReportService,
                                TestService testService,
                                AccountService accountService,
                                EbikeService ebikeService,
                                CustomerService customerService) {
        this.testReportService = testReportService;
        this.testService = testService;
        this.ebikeService = ebikeService;
        this.accountService = accountService;
        this.customerService = customerService;
    }
    @GetMapping("/test-results/{uuid}")
    public String showVisualisation(@PathVariable UUID uuid, Model model) {
        model.addAttribute("uuid", uuid);
        return "results/test_vis";
    }

    @GetMapping("/import")
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN')")
    public String uploadCsv(Model model) {
        List<Customer> cust = customerService.findAll();
        model.addAttribute("customers", cust);
        model.addAttribute("testTypes", TestType.values());
        model.addAttribute("inProgress", false);
        return "tech/import-test";
    }

    @PostMapping("/import")
    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN')")
    public ModelAndView importTestResults(
            @RequestParam("customerId") int customerId,
            @RequestParam("ebikeId") int ebikeId,
            @RequestParam("type") TestType type,
            @RequestParam(value = "batteryCapacity", required = false) Integer batteryCapacity,
            @RequestParam(value = "maxSupport", required = false) Integer maxSupport,
            @RequestParam(value = "enginePowerMax", required = false) Integer enginePowerMax,
            @RequestParam(value = "enginePowerNominal", required = false) Integer enginePowerNominal,
            @RequestParam(value = "engineTorque", required = false) Integer engineTorque,
            @RequestParam("testCsv") MultipartFile file,
            Principal principal
    ) throws IOException {

        Account technician = accountService.findByEmail(principal.getName());
        int technicianId = technician.getId();

        StartTestDto dto = new StartTestDto(
                type, batteryCapacity, maxSupport, enginePowerMax, enginePowerNominal, engineTorque,
                ebikeId, customerId, technicianId, ""
        );

        Test newTest = testService.createAndLinkTest(dto, true);
        testReportService.handleImport(file.getInputStream(), newTest.getUuid(), newTest);

        ModelAndView modelAndView = new ModelAndView("tech/import-test");
        modelAndView.getModel().put("inProgress", true);
        modelAndView.getModel().put("customers", customerService.findAll());
        return modelAndView;
    }

    @GetMapping("/technician/customers/{customerId}/bikes/{bikeId}/test-results/{uuid}")
    public String showVisualisation(
            @PathVariable UUID uuid,
            @PathVariable Integer customerId,
            @PathVariable Integer bikeId,
            Model model) {
        model.addAttribute("customerId", customerId);
        model.addAttribute("uuid", uuid);
        model.addAttribute("custBikeId", bikeId);

        return "results/test_vis";
    }



}