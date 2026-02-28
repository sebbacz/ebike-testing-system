package com.example.ebike_testing_system.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class EbikeModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    private int batteryCapacity;
    private int maxSupport;
    private int enginePowerMax;
    private int enginePowerNominal;
    private int engineTorque;

    @OneToMany(mappedBy = "eBikeModel")
    private List<Ebike> eBikes;

    public EbikeModel(int id, String name, int batteryCapacity, int maxSupport, int enginePowerMax,
                      int enginePowerNominal, int engineTorque, List<Ebike> eBikes) {
        this.id = id;
        this.name = name;
        this.batteryCapacity = batteryCapacity;
        this.maxSupport = maxSupport;
        this.enginePowerMax = enginePowerMax;
        this.enginePowerNominal = enginePowerNominal;
        this.engineTorque = engineTorque;
        this.eBikes = eBikes;

    }

    public EbikeModel() {
    }

    public List<Ebike> geteBikes() {
        return eBikes;
    }

    public void seteBikes(List<Ebike> eBikes) {
        this.eBikes = eBikes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
