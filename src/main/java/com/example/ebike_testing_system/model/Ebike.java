package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Ebike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT")
    // Let it generate int not bigint
    private int id;

    private String brand;
    private String model;
    private String battery;

    private String location;
    private LocalDate addedDate;

    private String qrCodeUrl;

    @ManyToOne
    @JoinColumn(name = "model_id") // FK column in Ebike table
    private EbikeModel eBikeModel;

    //    @JoinColumn(name = "customer_id")
    @OneToMany(mappedBy = "ebike")
    private List<CustomerBike> customerBike;

    @JsonBackReference
    @OneToMany(mappedBy = "ebike", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Test> tests = new ArrayList<>();

    public Ebike() {
    }

    public Ebike(int id, String brand, String model, String battery, EbikeModel eBikeModel,
                 List<CustomerBike> customerBike, List<Test> tests) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.battery = battery;
        this.eBikeModel = eBikeModel;
        this.customerBike = customerBike;
        this.tests = tests;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public EbikeModel getEbikeModel() {
        return eBikeModel;
    }

    public void seteBikeModel(EbikeModel eBikeModel) {
        this.eBikeModel = eBikeModel;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getQrCodeUrl() {
        return qrCodeUrl;
    }

    public void setQrCodeUrl(String qrCodeUrl) {
        this.qrCodeUrl = qrCodeUrl;
    }

    public LocalDate getAddedDate() {
        return addedDate;
    }

    public void setAddedDate(LocalDate assignedDate) {
        this.addedDate = assignedDate;
    }

    public List<CustomerBike> getCustomerBike() {
        return customerBike;
    }

    public void setCustomerBike(List<CustomerBike> customerBike) {
        this.customerBike = customerBike;
    }


    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }
}
