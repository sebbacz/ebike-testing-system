package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.model.Ebike;
import com.example.ebike_testing_system.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AdminDashboardController {

    @Autowired
    private AdminService adminService;

    @PreAuthorize("hasAnyRole('ADMIN') and principal.approved == true")
    @GetMapping("/admin/dashboard")
    public String getAdminDashboard(Model model) {
        model.addAttribute("testCount", adminService.getTestCountByAdminCompany());
        return "admin/admin_dashboard";
    }

    @PreAuthorize("hasAnyRole('ADMIN') and principal.approved == true")
    @GetMapping("/admin/dashboard/customers")
    public String getCustomers(Model model) {
        model.addAttribute("customers", adminService.getCustomersByAdminCompany());
        return "admin/admin_customers";
    }

    @PreAuthorize("hasAnyRole('ADMIN') and principal.approved == true")
    @GetMapping("/admin/dashboard/technicians")
    public String getTechnicians(Model model) {
        model.addAttribute("technicians", adminService.getAllTechnicians());
        return "admin/admin_technicians";
    }

    @PreAuthorize("hasAnyRole('ADMIN') and principal.approved == true")
    @GetMapping("/admin/dashboard/errorcodes")
    public String getErrorCodes(Model model) {
        model.addAttribute("errorCodes", adminService.getErrorCodesByAdminCompany());
        return "admin/admin_errorcodes";
    }

    @PreAuthorize("hasAnyRole('ADMIN') and principal.approved == true")
    @GetMapping("/admin/dashboard/bikes")
    public String getBikes(Model model) {
        model.addAttribute("bikes", adminService.getBikesByAdminCompany());
        return "admin/admin_bikes";
    }

    @PreAuthorize("hasAnyRole('ADMIN') and principal.approved == true")
    @PostMapping("/admin/dashboard/add-bike")
    public String addBike(@ModelAttribute Ebike bike, RedirectAttributes redirect) {
        adminService.addBike(bike);
        redirect.addFlashAttribute("msg", "Bike added successfully!");
        return "redirect:/admin/dashboard/bikes";
    }

    @PreAuthorize("hasAnyRole('ADMIN') and principal.approved == true")
    @PostMapping("/admin/dashboard/remove-bike/{bikeId}")
    public String removeBike(@PathVariable int bikeId, RedirectAttributes redirect) {
        adminService.removeBike(bikeId);
        redirect.addFlashAttribute("msg", "Bike removed successfully!");
        return "redirect:/admin/dashboard/bikes";
    }

    @PreAuthorize("hasAnyRole('ADMIN') and principal.approved == true")
    @GetMapping("/admin/dashboard/results")
    public String getTestResults(Model model) {
        model.addAttribute("testResults", adminService.getTestByAdminCompany());
        return "tech/list-tests";
    }
}
