package com.example.ebike_testing_system.dto;

import com.example.ebike_testing_system.model.CustomerBike;
import jakarta.persistence.OneToMany;

import java.util.List;

public class CustomerDto {

    private String name;
    private String phone;
    private String address;
    private int customerBikeId;


    public CustomerDto(String name, String phone, String address, int customerBikeId) {
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.customerBikeId = customerBikeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCustomerBikes() {
        return customerBikeId;
    }

    public void setCustomerBikes(int customerBikes) {
        this.customerBikeId = customerBikes;
    }


}
