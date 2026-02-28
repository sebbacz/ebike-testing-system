package com.example.ebike_testing_system.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@DiscriminatorValue("ADMIN")
public class Admin extends Account {

    private String name;
    private boolean isRoleApproved;

    public Admin(int id, String username, String password, String email, String name, boolean isRoleApproved) {
        super(id, username, password, email);
        this.name = name;
        this.isRoleApproved = isRoleApproved;
    }

    public Admin() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isRoleApproved() {
        return isRoleApproved;
    }

    public void setRoleApproved(boolean roleApproved) {
        isRoleApproved = roleApproved;
    }
}
