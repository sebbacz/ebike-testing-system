package com.example.ebike_testing_system.dto;

public class EbikeDto {

    private int id;
    private String brand;
    private String model;
    private String battery;
    private EbikeModelDto ebikeModelDto;
    private int customerCount;

    public String getEbikeModelName() {
        return ebikeModelDto != null ? ebikeModelDto.getName() : null;
    }
    public EbikeModelDto getEbikeModelDto() {
        return ebikeModelDto;
    }

    public void setEbikeModelDto(EbikeModelDto ebikeModelDto) {
        this.ebikeModelDto = ebikeModelDto;
    }

    private int modelId; // ID of the selected EbikeModel

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public int getModelId() {
        return modelId;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public int getCustomerCount() {
        return customerCount;
    }

    public void setCustomerCount(int customerCount) {
        this.customerCount = customerCount;
    }
}
