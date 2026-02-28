package com.example.ebike_testing_system.dto;

import com.example.ebike_testing_system.model.BearingType;

public class BearingThresholdDto {
    private BearingType bearingType;
    private double maxHorizontalVibration;
    private double maxVerticalVibration;

    public BearingType getBearingType() {
        return bearingType;
    }

    public void setBearingType(BearingType bearingType) {
        this.bearingType = bearingType;
    }

    public double getMaxHorizontalVibration() {
        return maxHorizontalVibration;
    }

    public void setMaxHorizontalVibration(double maxHorizontalVibration) {
        this.maxHorizontalVibration = maxHorizontalVibration;
    }

    public double getMaxVerticalVibration() {
        return maxVerticalVibration;
    }

    public void setMaxVerticalVibration(double maxVerticalVibration) {
        this.maxVerticalVibration = maxVerticalVibration;
    }
}
