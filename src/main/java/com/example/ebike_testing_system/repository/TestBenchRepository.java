package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.model.TestBench;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TestBenchRepository extends JpaRepository<TestBench, Long> {
    @Query("SELECT COUNT(tb) FROM TestBench tb WHERE tb.status = 'Active'")
    long countActiveTestBenches();

}