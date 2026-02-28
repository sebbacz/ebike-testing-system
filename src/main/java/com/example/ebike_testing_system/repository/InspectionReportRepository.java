package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.model.InspectionReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InspectionReportRepository extends JpaRepository<InspectionReport, Long> {

    // Finds all reports for a given customer bike
    List<InspectionReport> findByCustomerBike_Id(int customerBikeId);
    Optional<InspectionReport> findByTestUuid(UUID testUuid);
}