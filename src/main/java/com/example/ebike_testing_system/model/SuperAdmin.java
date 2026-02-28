package com.example.ebike_testing_system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("SUPERADMIN")
public class SuperAdmin extends Account {
    public SuperAdmin(int id, String username, String password, String email) {
        super(id, username, password, email);
    }
    public SuperAdmin() {
    }
}