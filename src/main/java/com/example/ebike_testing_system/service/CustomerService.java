package com.example.ebike_testing_system.service;

import com.example.ebike_testing_system.dto.EbikeModelDto;
import com.example.ebike_testing_system.exception.NotFoundException;
import com.example.ebike_testing_system.model.Customer;
import com.example.ebike_testing_system.model.CustomerBike;
import com.example.ebike_testing_system.model.Ebike;
import com.example.ebike_testing_system.model.EbikeModel;
import com.example.ebike_testing_system.repository.CustomerBikeRepository;
import com.example.ebike_testing_system.repository.CustomerRepository;
import com.example.ebike_testing_system.repository.EbikeModelRepository;
import jakarta.transaction.Transactional;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);

    private final CustomerRepository customerRepository;
    private final CustomerBikeRepository customerBikeRepository;

    private  EbikeModelRepository ebikeModelRepository;

    public CustomerService(CustomerRepository customerRepository, CustomerBikeRepository customerBikeRepository) {
        this.customerRepository = customerRepository;
        this.customerBikeRepository = customerBikeRepository;
    }

    public Customer findById(int id){
        return customerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(" Customer not found: " + id));
    }

    public List<Customer> findAll(){
        return customerRepository.findAll();
    }

    public Customer getCustomerWithBikes(int customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
    }



    public List<Ebike> findCustomerEbikes(int custId){
        List<Ebike> ebikes = customerRepository.findEbikesForCustomer(custId);
        return ebikes;
    }

    public void removeLink(int custId, int bikeId) {
        CustomerBike link = customerBikeRepository.findByCustomerIdAndEbikeId(custId, bikeId)
                .orElseThrow(() -> new NotFoundException("No such link"));
        customerBikeRepository.delete(link);
    }







}
