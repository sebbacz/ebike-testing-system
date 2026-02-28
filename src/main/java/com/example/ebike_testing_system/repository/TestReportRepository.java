package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.dto.TestReportDto;
import com.example.ebike_testing_system.model.Test;
import com.example.ebike_testing_system.model.TestReport;
import com.example.ebike_testing_system.model.TestReportId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface TestReportRepository extends JpaRepository<TestReport, TestReportId> {

    List<TestReport> findByUuid(UUID uuid);
    List<TestReport> findByTest_Id(int id);

    long countByTest(Test test);
    @Query("SELECT r FROM TestReport r JOIN FETCH r.test t JOIN FETCH t.customer c")
    List<TestReport> findAllWithTestAndCustomer();

}
