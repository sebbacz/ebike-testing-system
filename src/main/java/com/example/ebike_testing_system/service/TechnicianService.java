package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.model.Ebike;
import com.example.ebike_testing_system.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TechnicianService {

    private final CustomerRepository customerRepository;

    public TechnicianService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElse(null);
    }

    public List<Ebike> getCustomerBikesByEmail(String email) {
        return customerRepository.findByEmail(email)
                .map(customer -> customerRepository.findEbikesForCustomer(customer.getId()))
                .orElse(Collections.emptyList());
    }
}