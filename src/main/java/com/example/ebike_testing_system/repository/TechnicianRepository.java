package com.example.ebike_testing_system.repository;
import com.example.ebike_testing_system.model.Technician;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TechnicianRepository extends JpaRepository<Technician, Long> {
    List<Technician> findByIsRoleApproved(boolean isRoleApproved);
}