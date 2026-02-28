package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.service.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequestMapping("/")
public class HomeController {

    // Set currentUserName and currentUserRole globally for all pages
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

    @ModelAttribute("isApproved")
    public boolean addApprovalToModel() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails customUser) {
            return customUser.getApproved();
        }
        return false;
    }


    @GetMapping("/")
    public String homePage() {
        return "home/index";
    }

    @GetMapping("/error")
    public String showError() {
        return "error";
    }

    @GetMapping("/about")
    public String aboutPage() {
        return "home/about";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "home/contact";
    }


}
