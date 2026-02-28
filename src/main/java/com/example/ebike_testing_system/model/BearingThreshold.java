package com.example.ebike_testing_system.model;

import jakarta.persistence.*;

@Entity
@Table(name = "bearing_threshold")
public class BearingThreshold {

    @Id
    @Enumerated(EnumType.STRING)
    private BearingType bearingType;

    @Column(name = "max_horizontal_vibration", nullable = false)
    private double maxHorizontalVibration;

    @Column(name = "max_vertical_vibration", nullable = false)
    private double maxVerticalVibration;

    public BearingThreshold() {}

    public BearingThreshold(BearingType type, double maxHoriz, double maxVert) {
        this.bearingType = type;
        this.maxHorizontalVibration = maxHoriz;
        this.maxVerticalVibration = maxVert;
    }

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