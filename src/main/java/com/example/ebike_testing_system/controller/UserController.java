package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.model.Account;
import com.example.ebike_testing_system.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class UserController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/user/profile")
    public String userProfile(Authentication authentication, Model model) {
        String email = authentication.getName();
        Account user = accountService.findByEmail(email);
        model.addAttribute("user", user);
        return "auth/user_profile";
    }

    @PostMapping("/user/profile/upload-avatar")
    public String uploadAvatar(@RequestParam("avatar") MultipartFile avatar, Authentication authentication) {
        String email = authentication.getName();
        accountService.saveAvatar(email, avatar);
        return "redirect:/user/profile";
    }
}