package com.example.ebike_testing_system.dto;

import com.example.ebike_testing_system.model.EbikeModel;

public class EbikeModelDto {

    private int id;

    private String name;
    private int batteryCapacity;
    private int maxSupport;
    private int enginePowerMax;
    private int enginePowerNominal;
    private int engineTorque;

    private int ebikeCount;

    public EbikeModelDto() {
    }


    public EbikeModelDto(EbikeModel model) {
        this.id = model.getId();
        this.name = model.getName();
        this.batteryCapacity = model.getBatteryCapacity();
        this.maxSupport = model.getMaxSupport();
        this.enginePowerMax = model.getEnginePowerMax();
        this.enginePowerNominal = model.getEnginePowerNominal();
        this.engineTorque = model.getEngineTorque();
        this.ebikeCount = model.geteBikes() != null ? model.geteBikes().size() : 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getEbikeCount() {
        return ebikeCount;
    }

    public void setEbikeCount(int ebikeCount) {
        this.ebikeCount = ebikeCount;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }


}