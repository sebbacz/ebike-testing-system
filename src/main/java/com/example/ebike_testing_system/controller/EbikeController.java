package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.dto.EbikeModelDto;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.service.CustomerService;
import com.example.ebike_testing_system.service.EbikeModelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@PreAuthorize("hasAnyRole('TECHNICIAN') and principal.approved == true")
@RequestMapping("/technician")
public class EbikeController {

    private final EbikeModelService ebikeModelService;
    private final CustomerService customerService;

    public EbikeController(EbikeModelService ebikeModelService,
                           CustomerService customerService) {
        this.ebikeModelService = ebikeModelService;
        this.customerService = customerService;
    }

    @GetMapping("/models")
    public String listModels(Model model) {
        model.addAttribute("models", ebikeModelService.findAll());
        model.addAttribute("ebikeModelDto", new EbikeModelDto()); // Required for the modal form
        return "tech/list-models";
    }


    @GetMapping("/models/add")
    public String showAddModelForm(Model model) {
        model.addAttribute("ebikeModelDto", new EbikeModelDto());
        return "tech/list-models"; // We'll design this page next
    }

    @GetMapping("/ebikes")
    public String showAllBikes(Model model){
        model.addAttribute("ebikeModels", ebikeModelService.findAll());
        model.addAttribute("customers", customerService.findAll());
        return "tech/all-bikes";

    }

}
