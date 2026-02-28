package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.model.Ebike;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.model.Test;
import com.example.ebike_testing_system.service.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/technician")
public class TechnicianController {

    private final EbikeModelService ebikeModelService;
    private final EbikeService ebikeService;
    private final CustomerService customerService;
    private final TestService testService;

    private final TechnicianService technicianService;

    public TechnicianController(EbikeModelService ebikeModelService, EbikeService ebikeService,
                                CustomerService customerService,
                                TestService testService,
                                TechnicianService technicianService) {
        this.ebikeModelService = ebikeModelService;
        this.ebikeService = ebikeService;
        this.customerService = customerService;
        this.testService = testService;
        this.technicianService = technicianService;
    }

    @GetMapping("/customers/search")
    public String searchCustomerBikes(@RequestParam("email") String email, Model model) {
        Customer customer = technicianService.findCustomerByEmail(email);
        if (customer != null) {
            model.addAttribute("customer", customer);
            model.addAttribute("bikes", technicianService.getCustomerBikesByEmail(email));
            return "tech/customer-bikes";
        } else {
            model.addAttribute("error", "Customer not found for email: " + email);
            return "tech/customers";
        }
    }

    @PreAuthorize("hasRole('TECHNICIAN') and principal.approved == true")
    @GetMapping("/dashboard")
    public String getDashboard(Model model) {
        List<EbikeModel> models = Optional.ofNullable(ebikeModelService.findAll()).orElse(Collections.emptyList());
        List<Customer> customers = Optional.ofNullable(customerService.findAll()).orElse(Collections.emptyList());
        List<Ebike> bikes = Optional.ofNullable(ebikeService.findAll()).orElse(Collections.emptyList());
        List<Test> tests = Optional.ofNullable(testService.findAll()).orElse(Collections.emptyList());

        model.addAttribute("modelsCount", models.size());
        model.addAttribute("latestModelName", !models.isEmpty() ? models.get(models.size() - 1).getName() : "N/A");

        model.addAttribute("customersCount", customers.size());
        model.addAttribute("latestCustomerName", !customers.isEmpty() ? customers.get(customers.size() - 1).getName() : "N/A");

        model.addAttribute("bikesCount", bikes.size());
        model.addAttribute("popularBikeBrand", !bikes.isEmpty() ? bikes.get(bikes.size() - 1).getBrand() : "N/A");

        model.addAttribute("testsCount", tests.size());
        model.addAttribute("lastTest", !tests.isEmpty()
                ? tests.get(tests.size() - 1).getStartDate().format(DateTimeFormatter.ofPattern("mm/hh dd/M/yyyy"))
                : "N/A");

        return "tech/dashboard";
    }


}