package com.example.ebike_testing_system.controller;

import com.example.ebike_testing_system.service.AccountService;
import com.example.ebike_testing_system.model.Account;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ResetPasswordController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/reset_password")
    public String showResetPasswordForm(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        Account account = accountService.getByResetPasswordToken(token);
        if (account == null) {
            model.addAttribute("message", "Invalid Token");
            return "auth/reset_password_form";
        }
        model.addAttribute("token", token);
        return "auth/reset_password_form";
    }

    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String newPassword = request.getParameter("password");

        Account account = accountService.getByResetPasswordToken(token);
        if (account == null) {
            model.addAttribute("message", "Invalid Token");
            return "auth/reset_password_form";
        } else {
            accountService.updatePassword(account, newPassword);
            model.addAttribute("message", "You have successfully changed your password.");
        }

        return "auth/reset_password_form";
    }
}