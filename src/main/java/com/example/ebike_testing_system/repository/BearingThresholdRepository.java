package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.model.BearingThreshold;
import com.example.ebike_testing_system.model.BearingType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BearingThresholdRepository extends JpaRepository<BearingThreshold, BearingType> {
}
