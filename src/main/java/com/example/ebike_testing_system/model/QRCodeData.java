package com.example.ebike_testing_system.model;

public class QRCodeData {
    private int bikeId;
    private String url;

    public QRCodeData(int bikeId, String url) {
        this.bikeId = bikeId;
        this.url = url;
    }

    public int getBikeId() {
        return bikeId;
    }

    public void setBikeId(int bikeId) {
        this.bikeId = bikeId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}