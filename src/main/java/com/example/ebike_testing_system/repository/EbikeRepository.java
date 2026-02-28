package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.model.Ebike;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EbikeRepository extends JpaRepository<Ebike, Integer> {

    List<Ebike> findByBrandIgnoreCase(String brand);

    //did this to avoid LazyInitializationException error
    @Override
    @EntityGraph(attributePaths = {"customerBike", "customerBike.customer"})
    List<Ebike> findAll();
}
