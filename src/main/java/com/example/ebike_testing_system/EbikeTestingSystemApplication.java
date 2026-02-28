package com.example.ebike_testing_system;

import com.example.ebike_testing_system.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EbikeTestingSystemApplication{
//public class EbikeTestingSystemApplication implements CommandLineRunner {

//    @Autowired
//    private AccountService accountService;

    public static void main(String[] args) {
        SpringApplication.run(EbikeTestingSystemApplication.class, args);
    }

//    @Override
//    public void run(String... args) throws Exception {
//        accountService.createSuperAdmin();
//    }
}