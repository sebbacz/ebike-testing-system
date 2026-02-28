package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.model.Admin;
import com.example.ebike_testing_system.model.Technician;
import com.example.ebike_testing_system.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class SuperAdminController {

    @Autowired
    private AccountService accountService;

    @ModelAttribute
    public void setUserAttributes(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                model.addAttribute("currentUserName", userDetails.getUsername());
                if (userDetails.getAuthorities() != null && !userDetails.getAuthorities().isEmpty()) {
                    model.addAttribute("currentUserRole", userDetails.getAuthorities().iterator().next().getAuthority());
                } else {
                    model.addAttribute("currentUserRole", "Unknown");
                }
            }
        } else {
            model.addAttribute("currentUserName", null);
            model.addAttribute("currentUserRole", null);
        }
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @GetMapping("/superadmin/dashboard")
    public String showDashboard(Model model) {
        List<Admin> unapprovedAdmins = accountService.getUnapprovedAdmins();
        List<Technician> unapprovedTechnicians = accountService.getUnapprovedTechnicians();
        List<Admin> approvedAdmins = accountService.getApprovedAdmins();
        List<Technician> approvedTechnicians = accountService.getApprovedTechnicians();

        model.addAttribute("unapprovedAdmins", unapprovedAdmins);
        model.addAttribute("unapprovedTechnicians", unapprovedTechnicians);
        model.addAttribute("approvedAdmins", approvedAdmins);
        model.addAttribute("approvedTechnicians", approvedTechnicians);
        return "auth/superadmindashboard";
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @GetMapping("/superadmin/edit/{role}/{id}")
    public String editUser(@PathVariable("role") String role, @PathVariable("id") int id, Model model) {
        if ("admin".equals(role)) {
            model.addAttribute("user", accountService.getAdminById(id));
        } else if ("technician".equals(role)) {
            model.addAttribute("user", accountService.getTechnicianById(id));
        }
        model.addAttribute("role", role);
        return "auth/edit_user";
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PostMapping("/superadmin/edit")
    public String updateUser(@RequestParam("id") int id, @RequestParam("role") String role, @RequestParam("name") String name, @RequestParam("email") String email) {
        if ("admin".equals(role)) {
            accountService.updateAdmin(id, name, email);
        } else if ("technician".equals(role)) {
            accountService.updateTechnician(id, name, email);
        }
        return "redirect:/superadmin/dashboard";
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PostMapping("/superadmin/delete")
    public String deleteUser(@RequestParam("id") long id, @RequestParam("role") String role) {
        if ("admin".equals(role)) {
            accountService.deleteAdmin(id);
        } else if ("technician".equals(role)) {
            accountService.deleteTechnician(id);
        }
        return "redirect:/superadmin/dashboard";
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @PostMapping("/superadmin/approve")
    public String approveRole(@RequestParam("accountId") int accountId, @RequestParam("role") String role) {
        if ("admin".equals(role)) {
            accountService.approveAdmin(accountId);
        } else if ("technician".equals(role)) {
            accountService.approveTechnician(accountId);
        }
        return "redirect:/superadmin/dashboard";
    }

}