package com.example.ebike_testing_system.dto;

public interface EbikeModelProjection {
    int getId();
    String getName();
    int getBatteryCapacity();
    int getMaxSupport();
    int getEnginePowerMax();
    int getEnginePowerNominal();
    int getEngineTorque();
    int getEbikeCount();
}
