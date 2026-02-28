package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.model.FunctionalCheck;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FunctionalCheckRepository extends JpaRepository<FunctionalCheck, Long> {

}