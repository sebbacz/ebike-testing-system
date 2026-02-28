package com.example.ebike_testing_system.controller;


import com.example.ebike_testing_system.model.Admin;
import com.example.ebike_testing_system.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminApprovalController {

    @Autowired
    private AccountService accountService;
    @PreAuthorize("hasRole('SUPERADMIN')")
    @GetMapping("/approve")
    public String showApproveAdminsPage(Model model) {
        List<Admin> admins = accountService.getAllAdmins();
        model.addAttribute("admins", admins);
        return "auth/approve";
    }

    @PostMapping("/approve")
    public String approveAdmin(@RequestParam("adminId") int adminId) {
        accountService.approveAdmin(adminId);
        return "redirect:/admin/approve";
    }
}
