package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.model.Test;
import com.example.ebike_testing_system.model.TestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
@Repository
public interface TestRepository extends JpaRepository<Test, Integer> {
    Optional<Test> findByUuid(UUID uuid);
    List<Test> findByStatus(TestStatus status);
    List<Test> findByCustomerId(int id);

    List<Test> findByEbikeId(int ebikeId);




}
