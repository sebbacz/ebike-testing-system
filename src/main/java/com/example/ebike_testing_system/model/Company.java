package com.example.ebike_testing_system.model;


import jakarta.persistence.*;

@Entity
@DiscriminatorValue("company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String companyName;
}
