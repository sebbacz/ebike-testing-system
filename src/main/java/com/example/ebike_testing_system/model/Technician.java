package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("TECHNICIAN")
public class Technician extends Account {

    private String name;
    @Column(nullable = true)
    private int experienceYears;
    @OneToMany(mappedBy = "technician", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonBackReference
    private List<Test> tests = new ArrayList<>();

    private boolean isRoleApproved;

    public Technician(int id, String username, String password, String email,
                      String name,
                      int experienceYears,
                      List<Test> tests, boolean isRoleApproved) {
        super(id, username, password, email);
        this.name = name;
        this.experienceYears = experienceYears;
        this.tests = tests;
        this.isRoleApproved = isRoleApproved;
    }

    public Technician(){
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public List<Test> getTests() {
        return tests;
    }

    public void setTests(List<Test> tests) {
        this.tests = tests;
    }

    public boolean isRoleApproved() {
        return isRoleApproved;
    }
    public void setApproved(boolean approved) {
        isRoleApproved = approved;
    }

    public void setRoleApproved(boolean roleApproved) {
        isRoleApproved = roleApproved;
    }
}
