package com.example.ebike_testing_system.repository;

import com.example.ebike_testing_system.dto.EbikeModelProjection;
import com.example.ebike_testing_system.model.EbikeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EbikeModelRepository extends JpaRepository<EbikeModel, Integer> {

    List<EbikeModel> findByNameContainingIgnoreCase(String name);


    @Query("""
    SELECT 
        m.id as id,
        m.name AS name,
        m.batteryCapacity AS batteryCapacity,
        m.maxSupport AS maxSupport,
        m.enginePowerMax AS enginePowerMax,
        m.enginePowerNominal AS enginePowerNominal,
        m.engineTorque AS engineTorque,
        SIZE(m.eBikes) AS ebikeCount
    FROM EbikeModel m
""")
    List<EbikeModelProjection> findAllProjected();


    @Query("""
    SELECT 
        m.id as id,
        m.name AS name,
        m.batteryCapacity AS batteryCapacity,
        m.maxSupport AS maxSupport,
        m.enginePowerMax AS enginePowerMax,
        m.enginePowerNominal AS enginePowerNominal,
        m.engineTorque AS engineTorque,
        SIZE(m.eBikes) AS ebikeCount
    FROM EbikeModel m
    WHERE m.id = :id
""")
    Optional<EbikeModelProjection> findProjectedById(@Param("id") int id);
}
