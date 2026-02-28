package com.example.ebike_testing_system.dto;

import com.example.ebike_testing_system.model.TestStatus;
import com.example.ebike_testing_system.model.TestType;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestDto {

    private int id;
    private UUID uuid;
    private TestType type;
    private int batteryCapacity;
    private int maxSupport;
    private int enginePowerMax;
    private int enginePowerNominal;
    private int engineTorque;

    private TestStatus status;
    private String notes;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int ebikeId;
    private int customerId;
    private int technicianId;

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getEbikeId() {
        return ebikeId;
    }

    public void setEbikeId(int ebikeId) {
        this.ebikeId = ebikeId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getTechnicianId() {
        return technicianId;
    }

    public void setTechnicianId(int technicianId) {
        this.technicianId = technicianId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public int getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(int batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public int getMaxSupport() {
        return maxSupport;
    }

    public void setMaxSupport(int maxSupport) {
        this.maxSupport = maxSupport;
    }

    public int getEnginePowerMax() {
        return enginePowerMax;
    }

    public void setEnginePowerMax(int enginePowerMax) {
        this.enginePowerMax = enginePowerMax;
    }

    public int getEnginePowerNominal() {
        return enginePowerNominal;
    }

    public void setEnginePowerNominal(int enginePowerNominal) {
        this.enginePowerNominal = enginePowerNominal;
    }

    public int getEngineTorque() {
        return engineTorque;
    }

    public void setEngineTorque(int engineTorque) {
        this.engineTorque = engineTorque;
    }


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}
