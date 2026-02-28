package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.model.Test;
import com.example.ebike_testing_system.service.TestService;
import com.example.ebike_testing_system.service.TestbenchService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class TestsController {

    private final TestService testService;

    public TestsController(TestService testService) {
        this.testService = testService;
    }

    @PreAuthorize("hasAnyRole('TECHNICIAN', 'ADMIN') and principal.approved == true")
    @GetMapping("/technician/all-tests")
    public String listAllTests(Model model){
        model.addAttribute("allTests", testService.findAll());
        return "tech/list-tests";
    }

    @GetMapping("/technician/customers/{userId}/tests")
    public String listTestsForCustomer(Model model, @PathVariable int userId){
        List<Test> test = testService.findByCustomerId(userId);
        int ebikeId = 0;
        for (Test var : test){
            ebikeId = var.getEbike().getId();
        }
        List<Test> allTests = testService.findAll();
        model.addAttribute("tests", allTests);

        model.addAttribute("customerId", userId); // expose ID to view
        model.addAttribute("ebikeId", ebikeId); // expose ID to view
        model.addAttribute("testsForYou", test);
        return "tech/list-tests";
    }

    @GetMapping("/technician/bikes/{bikeId}/tests")
    public String listTestsForBike(Model model, @PathVariable int bikeId){
        model.addAttribute("bikeId", bikeId); // expose ID to view
        model.addAttribute("testsForBike", testService.findByBikeId(bikeId));
        return "tech/customer-tests";
    }


//    @GetMapping("/user/{userId}/tests")
//    public String listTestsForCustomer(Model model, @PathVariable int userId){
//        model.addAttribute("customerId", userId); // expose ID to view
//        model.addAttribute("testsForYou", testService.findByCustomerId(userId));
//        return "/tech/list-tests";
//    }

}
