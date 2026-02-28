package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

@Entity
@Table(name = "CustBike")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class CustomerBike {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // auto-generated primary key

    @ManyToOne(optional = false)
    @JoinColumn(name = "ebike_id")
    private Ebike ebike;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private EbikeModel ebikeModel;

    @ManyToOne(optional = false)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public CustomerBike() {
    }

    public CustomerBike(Ebike ebike, Customer customer) {
        this.ebike = ebike;
        this.customer = customer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Ebike getEbike() {
        return ebike;
    }

    public void setEbike(Ebike ebike) {
        this.ebike = ebike;
    }

    public Customer getCustomer() {
        return customer;
    }

    public EbikeModel getEbikeModel() {
        return ebikeModel;
    }

    public void setEbikeModel(EbikeModel ebikeModel) {
        this.ebikeModel = ebikeModel;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


}
