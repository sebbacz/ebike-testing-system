package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.service.CustomUserDetails;
import com.example.ebike_testing_system.service.CustomerService;
import com.example.ebike_testing_system.service.EbikeModelService;
import com.example.ebike_testing_system.service.EbikeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

import java.util.List;

@Controller
//@PreAuthorize("hasAnyRole('TECHNICIAN') and principal.approved == true")
@RequestMapping()
public class CustomerController {


    private final CustomerService customerService;
    private final EbikeService ebikeService;
    private final EbikeModelService ebikeModelService;

    public CustomerController(CustomerService customerService, EbikeService ebikeService, EbikeModelService ebikeModelService) {
        this.customerService = customerService;
        this.ebikeService = ebikeService;
        this.ebikeModelService = ebikeModelService;
    }
    @PreAuthorize("hasAnyRole('TECHNICIAN') and principal.approved == true")
    @GetMapping({"/customers", "/technician/customers"})
    public String showCustomers(Model model) {
        model.addAttribute("customers", customerService.findAll());
        return "tech/customers";
    }


    // Show bikes for a customer
    @GetMapping("/technician/customers/{custId}/bikes")
    public String listCustomerBikes(@PathVariable int custId, Model model) {
        Customer customer = customerService.getCustomerWithBikes(custId);
        List<EbikeModel> ebikeModels = ebikeModelService.findAll();
        model.addAttribute("customer", customer);
        model.addAttribute("ebikeModels", ebikeModels);
        return "tech/customer-bikes";
    }

    // Show form to add new e-bike (GET)
    @PreAuthorize("hasAnyRole('TECHNICIAN') and principal.approved == true")
    @GetMapping("/{custId}/bikes/add")
    public String showAddBikeForm(@PathVariable int custId, Model model) {
        model.addAttribute("ebikeModels", ebikeModelService.findAll());
        model.addAttribute("customerId", custId);
        return "tech/customer-bikes";
    }


    @GetMapping("/customers/me/bikes")
    @PreAuthorize("hasRole('CUSTOMER')")
    public String listOwnBikes(Principal principal, Model model) {
        int customerId = ((CustomUserDetails) ((Authentication) principal).getPrincipal()).getCustomUserId();
        Customer customer = customerService.getCustomerWithBikes(customerId);
        model.addAttribute("customer", customer);
        return "tech/qr-qr";
    }




    // Process bike addition (POST)
//    @PostMapping("/{custId}/bikes/add")
//    public String addBikeForCustomer(@PathVariable int custId, @ModelAttribute EbikeDto dto) {
//        ebikeService.addBikeForCustomer(custId, dto);
//        return "redirect:/customers/" + custId + "/bikes";
//    }


}
