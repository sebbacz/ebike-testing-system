package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;


@Entity
@DiscriminatorValue("CUSTOMER")
public class Customer extends Account {

    private String name;
    private String phone;
    private String address;

//    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "customer", fetch = FetchType.EAGER)
    private List<CustomerBike> customerBikes;

    public Customer(int id, String username, String password, String email,
                    String name, String phone, String address, List<CustomerBike> customerBikes) {
        super(id, username, password, email);
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.customerBikes = customerBikes;
    }

    public Customer() {}

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


    public List<CustomerBike> getCustomerBikes() {
        return customerBikes;
    }

    public void setCustomerBikes(List<CustomerBike> customerBikes) {
        this.customerBikes = customerBikes;
    }
}

