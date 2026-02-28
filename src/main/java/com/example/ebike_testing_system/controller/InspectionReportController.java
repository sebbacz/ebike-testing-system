package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.dto.InspectionReportDto;
import com.example.ebike_testing_system.dto.TestReportDto;
import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.repository.CustomerBikeRepository;
import com.example.ebike_testing_system.repository.EbikeRepository;
import com.example.ebike_testing_system.repository.TestReportRepository;
import com.example.ebike_testing_system.repository.TestRepository;
import com.example.ebike_testing_system.service.InspectionReportService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
//@RequestMapping("/technician")
public class InspectionReportController {

    private final InspectionReportService reportService;
    private final TestRepository testRepo;
    private final TestReportRepository testReportRepo;
    private final CustomerBikeRepository customerBikeRepo;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;



    public InspectionReportController(InspectionReportService reportService,
                                      TestRepository testRepo,
                                      TestReportRepository testReportRepo,
                                      ObjectMapper objectMapper,
                                      ModelMapper modelMapper,
                                      CustomerBikeRepository customerBikeRepo) {
        this.reportService = reportService;
        this.testRepo = testRepo;
        this.testReportRepo = testReportRepo;
        this.customerBikeRepo = customerBikeRepo;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
    }

    @PreAuthorize("hasRole('TECHNICIAN') and principal.approved == true")
    @GetMapping("/technician/reports/new/{testUuid}")
    public String showReportForm(@PathVariable UUID testUuid, Model model) {
        // Load test and related data
        Test test = testRepo.findByUuid(testUuid)
                .orElseThrow(() -> new NotFoundException("Test not found"));

        Ebike ebike = test.getEbike();
        Customer customer = test.getCustomer();

        // Retrieve the CustomerBike ID based on the Ebike and Customer from the test
        int customerBikeId = customerBikeRepo.findByEbikeAndCustomer(ebike, customer)
                .orElseThrow(() -> new NotFoundException("Customer bike not found for this test's ebike and customer"))
                .getId(); // Get the ID from the CustomerBike entity

        InspectionReportDto reportDto = reportService.generateDto(testUuid, new InspectionReportDto());


        // Prepare visual inspection items (translated to English but will show Dutch labels)
        List<ChecklistItem> visualInspectionItems = Arrays.asList(
                new ChecklistItem("Tires"),
                new ChecklistItem("Bell"),
                new ChecklistItem("Cranks"),
                new ChecklistItem("Electrical Wiring"),
                new ChecklistItem("Frame/Front Fork"),
                new ChecklistItem("Handlebars"),
                new ChecklistItem("Chain/Belt"),
                new ChecklistItem("Pedals"),
                new ChecklistItem("Reflectors"),
                new ChecklistItem("Brake Pads"),
                new ChecklistItem("Brake Levers"),
                new ChecklistItem("Brake Cables"),
                new ChecklistItem("Brake Discs"),
                new ChecklistItem("Gear Cables"),
                new ChecklistItem("Fenders"),
                new ChecklistItem("Handlebar/Stem"),
                new ChecklistItem("Rear Sprocket"),
                new ChecklistItem("Front Sprocket"),
                new ChecklistItem("Rims/Spokes"),
                new ChecklistItem("Rear Suspension"),
                new ChecklistItem("Front Suspension"),
                new ChecklistItem("Saddle")
        );

        // Prepare functional check items
        List<ChecklistItem> functionalCheckItems = Arrays.asList(
                new ChecklistItem("Rear Light"),
                new ChecklistItem("Rear Brake"),
                new ChecklistItem("Derailleur Gears"),
                new ChecklistItem("Display"),
                new ChecklistItem("Phone Holder"),
                new ChecklistItem("Hub Gears"),
                new ChecklistItem("Front Light"),
                new ChecklistItem("Front Brake"),
                new ChecklistItem("Seat Suspension")
        );

        // Fetch TestReport rows for test procedures table
        List<TestReport> testReportRows = testReportRepo.findByUuid(testUuid);
        List<TestReportDto> testReportDtos = testReportRows.stream().map(tr -> {
            TestReportDto dto = modelMapper.map(tr, TestReportDto.class);
            return dto;
        }).toList();


        // Convert lists and necessary DTO parts to JSON strings
        try {
            model.addAttribute("visualInspectionItemsJson", objectMapper.writeValueAsString(visualInspectionItems));
            model.addAttribute("functionalCheckItemsJson", objectMapper.writeValueAsString(functionalCheckItems));
//            model.addAttribute("testReportRowsJson", objectMapper.writeValueAsString(testReportDtos)); // Pass test reports for JS
            model.addAttribute("testProceduresJson", objectMapper.writeValueAsString(reportDto.getTestProcedures())); // <-- CHANGED
            model.addAttribute("bearingConditionsJson", objectMapper.writeValueAsString(reportDto.getBearingConditions()));
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Log the error for debugging
            model.addAttribute("visualInspectionItemsJson", "[]");
            model.addAttribute("functionalCheckItemsJson", "[]");
            model.addAttribute("testReportRowsJson", "[]");
            model.addAttribute("bearingConditionsJson", "[]");
        }


        // Add data to model for Thymeleaf (for direct access or JS inline)
        model.addAttribute("currentDate", LocalDate.now()); // Add current date to the model
        model.addAttribute("testUuid", testUuid);
        model.addAttribute("ebike", ebike);
        model.addAttribute("customer", customer);
        model.addAttribute("customerBikeId", customerBikeId);
        model.addAttribute("test", test); // Pass the Test object for its properties

        // Add pre-calculated DTO values to the model
        model.addAttribute("reportDto", reportDto); // Pass the entire DTO

        return "reports/inspection-report";
    }

    @GetMapping("/reports/{testUuid}") //
    public String displayReport(@PathVariable UUID testUuid, Model model) {
        model.addAttribute("testUuid", testUuid);
        return "reports/display-report";
    }

    @GetMapping("/technician/reports/success")
    public String showSuccessPage() {
        return "reports/success";
    }


}