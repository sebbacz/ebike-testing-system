package com.example.ebike_testing_system.dto;

public class BearingAnalysisDto {
    private String frontAxelCondition;  // "good" or "bad"
    private String rearAxelCondition;
    private String motorCondition;
    private double overallScore;  // Percentage score (0-100)

    public BearingAnalysisDto(String frontAxelCondition, String rearAxelCondition, String motorCondition, double overallScore) {
        this.frontAxelCondition = frontAxelCondition;
        this.rearAxelCondition = rearAxelCondition;
        this.motorCondition = motorCondition;
        this.overallScore = overallScore;
    }

    public String getFrontAxelCondition() {
        return frontAxelCondition;
    }

    public void setFrontAxelCondition(String frontAxelCondition) {
        this.frontAxelCondition = frontAxelCondition;
    }

    public String getRearAxelCondition() {
        return rearAxelCondition;
    }

    public void setRearAxelCondition(String rearAxelCondition) {
        this.rearAxelCondition = rearAxelCondition;
    }

    public String getMotorCondition() {
        return motorCondition;
    }

    public void setMotorCondition(String motorCondition) {
        this.motorCondition = motorCondition;
    }

    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }
}
