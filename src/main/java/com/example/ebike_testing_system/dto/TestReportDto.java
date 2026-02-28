package com.example.ebike_testing_system.dto;

import com.example.ebike_testing_system.model.Test;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.time.LocalDateTime;
import java.util.UUID;

public class TestReportDto {

    private UUID uuid;
    private LocalDateTime timestamp;

    private String errorCode;
    private double batteryVoltage; // 23.0
    private int batteryCurrent; // 17
    private double batteryCapacity; // 37.313248 (changed to double)
    private double batteryTemperatureCelsius; // 66.0
    private int chargeStatus;
    private int assistanceLevel; // 4
    private double torqueCrankNm; // 1.001078
    private double bikeWheelSpeedKmh; // 24.019865
    private int cadanceRpm; // 62
    private int engineRpm; // 1296
    private double enginePowerWatt; // 480.0
    private double wheelPowerWatt; // 240.0
    private double rollTorque; // 120.0
    private double loadcellN; // 150.0
    private double rolHz; // 3553.1567
    private double horizontalInclination; // 50.08754
    private double verticalInclination; // 6.0420723
    private int loadPower; // 21
    private boolean statusPlug; // false

    public TestReportDto() {
    }

    public TestReportDto(UUID uuid, LocalDateTime timestamp, String errorCode,
                         double batteryVoltage, int batteryCurrent, double batteryCapacity,
                         double batteryTemperatureCelsius, int chargeStatus, int assistanceLevel,
                         double torqueCrankNm, double bikeWheelSpeedKmh, int cadanceRpm,
                         int engineRpm, double enginePowerWatt, double wheelPowerWatt,
                         double rollTorque, double loadcellN, double rolHz,
                         double horizontalInclination, double verticalInclination,
                         int loadPower, boolean statusPlug) {
        this.uuid = uuid;
        this.timestamp = timestamp;
        this.errorCode = errorCode;
        this.batteryVoltage = batteryVoltage;
        this.batteryCurrent = batteryCurrent;
        this.batteryCapacity = batteryCapacity;
        this.batteryTemperatureCelsius = batteryTemperatureCelsius;
        this.chargeStatus = chargeStatus;
        this.assistanceLevel = assistanceLevel;
        this.torqueCrankNm = torqueCrankNm;
        this.bikeWheelSpeedKmh = bikeWheelSpeedKmh;
        this.cadanceRpm = cadanceRpm;
        this.engineRpm = engineRpm;
        this.enginePowerWatt = enginePowerWatt;
        this.wheelPowerWatt = wheelPowerWatt;
        this.rollTorque = rollTorque;
        this.loadcellN = loadcellN;
        this.rolHz = rolHz;
        this.horizontalInclination = horizontalInclination;
        this.verticalInclination = verticalInclination;
        this.loadPower = loadPower;
        this.statusPlug = statusPlug;
    }
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public double getBatteryVoltage() {
        return batteryVoltage;
    }

    public void setBatteryVoltage(double batteryVoltage) {
        this.batteryVoltage = batteryVoltage;
    }

    public int getBatteryCurrent() {
        return batteryCurrent;
    }

    public void setBatteryCurrent(int batteryCurrent) {
        this.batteryCurrent = batteryCurrent;
    }

    public double getBatteryCapacity() {
        return batteryCapacity;
    }

    public void setBatteryCapacity(double batteryCapacity) {
        this.batteryCapacity = batteryCapacity;
    }

    public double getBatteryTemperatureCelsius() {
        return batteryTemperatureCelsius;
    }

    public void setBatteryTemperatureCelsius(double batteryTemperatureCelsius) {
        this.batteryTemperatureCelsius = batteryTemperatureCelsius;
    }

    public int getChargeStatus() {
        return chargeStatus;
    }

    public void setChargeStatus(int chargeStatus) {
        this.chargeStatus = chargeStatus;
    }

    public int getAssistanceLevel() {
        return assistanceLevel;
    }

    public void setAssistanceLevel(int assistanceLevel) {
        this.assistanceLevel = assistanceLevel;
    }

    public double getTorqueCrankNm() {
        return torqueCrankNm;
    }

    public void setTorqueCrankNm(double torqueCrankNm) {
        this.torqueCrankNm = torqueCrankNm;
    }

    public double getBikeWheelSpeedKmh() {
        return bikeWheelSpeedKmh;
    }

    public void setBikeWheelSpeedKmh(double bikeWheelSpeedKmh) {
        this.bikeWheelSpeedKmh = bikeWheelSpeedKmh;
    }

    public int getCadanceRpm() {
        return cadanceRpm;
    }

    public void setCadanceRpm(int cadanceRpm) {
        this.cadanceRpm = cadanceRpm;
    }

    public int getEngineRpm() {
        return engineRpm;
    }

    public void setEngineRpm(int engineRpm) {
        this.engineRpm = engineRpm;
    }

    public double getEnginePowerWatt() {
        return enginePowerWatt;
    }

    public void setEnginePowerWatt(double enginePowerWatt) {
        this.enginePowerWatt = enginePowerWatt;
    }

    public double getWheelPowerWatt() {
        return wheelPowerWatt;
    }

    public void setWheelPowerWatt(double wheelPowerWatt) {
        this.wheelPowerWatt = wheelPowerWatt;
    }

    public double getRollTorque() {
        return rollTorque;
    }

    public void setRollTorque(double rollTorque) {
        this.rollTorque = rollTorque;
    }

    public double getLoadcellN() {
        return loadcellN;
    }

    public void setLoadcellN(double loadcellN) {
        this.loadcellN = loadcellN;
    }

    public double getRolHz() {
        return rolHz;
    }

    public void setRolHz(double rolHz) {
        this.rolHz = rolHz;
    }

    public double getHorizontalInclination() {
        return horizontalInclination;
    }

    public void setHorizontalInclination(double horizontalInclination) {
        this.horizontalInclination = horizontalInclination;
    }

    public double getVerticalInclination() {
        return verticalInclination;
    }

    public void setVerticalInclination(double verticalInclination) {
        this.verticalInclination = verticalInclination;
    }

    public int getLoadPower() {
        return loadPower;
    }

    public void setLoadPower(int loadPower) {
        this.loadPower = loadPower;
    }

    public boolean isStatusPlug() {
        return statusPlug;
    }

    public void setStatusPlug(boolean statusPlug) {
        this.statusPlug = statusPlug;
    }


}
