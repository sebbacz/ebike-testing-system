package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    List<Admin> findByIsRoleApproved(boolean isRoleApproved);
    Admin findByEmail(String email);

}