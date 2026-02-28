package com.example.ebike_testing_system.controller;
import com.example.ebike_testing_system.model.*;
import com.example.ebike_testing_system.repository.AccountRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AccountRepository accountRepository;

    @GetMapping("/signup")
    public String signupPage(Model model) {
        model.addAttribute("account", new Account());

        List<String> companies = new ArrayList<>(
                accountRepository.findAll().stream()
                        .filter(acc -> acc.getRole().toString().equalsIgnoreCase("ADMIN"))
                        .map(Account::getCompanyName)
                        .filter(name -> name != null && !name.trim().isEmpty())
                        .map(String::trim)
                        .map(name -> name.substring(0, 1).toUpperCase() + name.substring(1).toLowerCase())
                        .collect(Collectors.toCollection(() -> new TreeSet<>(String.CASE_INSENSITIVE_ORDER)))
        );

        model.addAttribute("companyList", companies);

        return "auth/signup";
    }


    @PostMapping("/process_signup")
    public String processRegister(Account account) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(account.getPassword());
        account.setPassword(encodedPassword);

        Account newAccount;
        switch (account.getRole().toString().toLowerCase()) {
            case "technician":
                newAccount = new Technician();
                ((Technician) newAccount).setName(account.getFullName());
                break;
            case "customer":
                newAccount = new Customer();
                ((Customer) newAccount).setName(account.getFullName());
                break;
            case "admin":
                newAccount = new Admin();
                ((Admin) newAccount).setName(account.getFullName());
                break;
            default:
                newAccount = new Account();
                break;
        }
        newAccount.setFullName(account.getFullName());
        newAccount.setPassword(account.getPassword());
        newAccount.setEmail(account.getEmail());
        newAccount.setCompanyName(account.getCompanyName());
        newAccount.setRole(account.getRole());

        accountRepository.save(newAccount);

        return "auth/register_success";
    }

    @PreAuthorize("hasAnyRole('SUPERADMIN')")
    @GetMapping("/superadmindashboard")
    public String superAdminDashboard(Model model) {
        return "auth/superadmindashboard";
    }


    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(Model model){ return "auth/denied";}

}