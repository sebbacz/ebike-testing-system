package com.example.ebike_testing_system.dto;

import com.example.ebike_testing_system.model.ConditionState;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InspectionReportDto {

    // Identification
    private UUID testUuid;
    private String brand;
    private String model;
    private LocalDate firstRegistration;
    private LocalDateTime createdOn;

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    private String battery;

    // Unique identifier of the report (for viewing existing reports)
    private long reportId;

    // The ID of the CustomerBike this report belongs to
    private int customerBikeId;

    // Overall comment
    private String comment;

    // Engine information
    private String motorModel;
    private int batteryCapacityWh;
    private int maxSupportPct;
    private int enginePowerMaxW;
    private int enginePowerNomW;
    private int engineTorqueNm;

    // Aggregate scores
    private double testProcScore;
    private int nominalLoadContW;
    private double nominalTempRiseCPerWh;
    private double nominalLoadScore;
    private int batteryTestCapacityWh;
    private int batteryTestHealthPct;
    private double batteryTestScore;
    private double bearingScore;
    private double overallScore;

    public UUID getTestUuid() {
        return testUuid;
    }

    public void setTestUuid(UUID testUuid) {
        this.testUuid = testUuid;
    }
    public double getOverallScore() {
        return overallScore;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }

    // Visual inspection rows
    private List<ChecklistRow> visualInspection = new ArrayList<>();

    // Test procedure entries
    private List<ProcedureRow> testProcedures = new ArrayList<>();

    // Bearing condition entries
    private List<ConditionRow> bearingConditions = new ArrayList<>();

    // Functional checks rows
    private List<ChecklistRow> functionalChecks = new ArrayList<>();

    public InspectionReportDto() {
    }

    // Identification getters/setters
    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public String getModel() { return model; }
    public void setModel(String type) { this.model = type; }

    public LocalDate getFirstRegistration() { return firstRegistration; }
    public void setFirstRegistration(LocalDate firstRegistration) { this.firstRegistration = firstRegistration; }

    public String getBattery() { return battery; }
    public void setBattery(String battery) { this.battery = battery; }

    public String getMotorModel() { return motorModel; }
    public void setMotorModel(String motorModel) { this.motorModel = motorModel; }

    // reportId / customerBikeId
    public Long getReportId() { return reportId; }
    public void setReportId(Long reportId) { this.reportId = reportId; }

    public Integer getCustomerBikeId() { return customerBikeId; }
    public void setCustomerBikeId(Integer customerBikeId) { this.customerBikeId = customerBikeId; }

    // Comment
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public Integer getBatteryCapacityWh() { return batteryCapacityWh; }
    public void setBatteryCapacityWh(Integer batteryCapacityWh) { this.batteryCapacityWh = batteryCapacityWh; }

    public Integer getMaxSupportPct() { return maxSupportPct; }
    public void setMaxSupportPct(Integer maxSupportPct) { this.maxSupportPct = maxSupportPct; }

    public Integer getEnginePowerMaxW() { return enginePowerMaxW; }
    public void setEnginePowerMaxW(Integer enginePowerMaxW) { this.enginePowerMaxW = enginePowerMaxW; }

    public Integer getEnginePowerNomW() { return enginePowerNomW; }
    public void setEnginePowerNomW(Integer enginePowerNomW) { this.enginePowerNomW = enginePowerNomW; }

    public Integer getEngineTorqueNm() { return engineTorqueNm; }
    public void setEngineTorqueNm(Integer engineTorqueNm) { this.engineTorqueNm = engineTorqueNm; }

    // Aggregate scores
    public Double getTestProcScore() { return testProcScore; }
    public void setTestProcScore(Double testProcScore) { this.testProcScore = testProcScore; }

    public Integer getNominalLoadContW() { return nominalLoadContW; }
    public void setNominalLoadContW(Integer nominalLoadContW) { this.nominalLoadContW = nominalLoadContW; }

    public Double getNominalTempRiseCPerWh() { return nominalTempRiseCPerWh; }
    public void setNominalTempRiseCPerWh(Double nominalTempRiseCPerWh) { this.nominalTempRiseCPerWh = nominalTempRiseCPerWh; }

    public Double getNominalLoadScore() { return nominalLoadScore; }
    public void setNominalLoadScore(Double nominalLoadScore) { this.nominalLoadScore = nominalLoadScore; }

    public Integer getBatteryTestCapacityWh() { return batteryTestCapacityWh; }
    public void setBatteryTestCapacityWh(Integer batteryTestCapacityWh) { this.batteryTestCapacityWh = batteryTestCapacityWh; }

    public Integer getBatteryTestHealthPct() { return batteryTestHealthPct; }
    public void setBatteryTestHealthPct(Integer batteryTestHealthPct) { this.batteryTestHealthPct = batteryTestHealthPct; }

    public Double getBatteryTestScore() { return batteryTestScore; }
    public void setBatteryTestScore(Double batteryTestScore) { this.batteryTestScore = batteryTestScore; }

    public Double getBearingScore() { return bearingScore; }
    public void setBearingScore(Double bearingScore) { this.bearingScore = bearingScore; }

    // Collections
    public List<ChecklistRow> getVisualInspection() { return visualInspection; }
    public void setVisualInspection(List<ChecklistRow> visualInspection) {
        this.visualInspection = Optional.ofNullable(visualInspection).orElse(new ArrayList<>());
    }

    public List<ProcedureRow> getTestProcedures() { return testProcedures; }
    public void setTestProcedures(List<ProcedureRow> testProcedures) {
        this.testProcedures = Optional.ofNullable(testProcedures).orElse(new ArrayList<>());
    }

    public List<ConditionRow> getBearingConditions() { return bearingConditions; }
    public void setBearingConditions(List<ConditionRow> bearingConditions) {
        this.bearingConditions = Optional.ofNullable(bearingConditions).orElse(new ArrayList<>());
    }

    public List<ChecklistRow> getFunctionalChecks() { return functionalChecks; }
    public void setFunctionalChecks(List<ChecklistRow> functionalChecks) {
        this.functionalChecks = Optional.ofNullable(functionalChecks).orElse(new ArrayList<>());
    }

    // Nested DTO classes
    public static class ChecklistRow {
        private String label;
        private ConditionState state;

        public ChecklistRow() {}

        public ChecklistRow(String label, ConditionState state) {
            this.label = label;
            this.state = state;
        }

        public String getLabel() { return label; }
        public void setLabel(String label) { this.label = label; }

        public ConditionState getState() { return state; }
        public void setState(ConditionState state) { this.state = state; }
    }

    public static class ProcedureRow {
        private String name;
        private Double result;
        private Double deviationPct;
        private String unit;

        public ProcedureRow() {}

        public ProcedureRow(String name, Double result, Double deviationPct, String unit) {
            this.name = name;
            this.result = result;
            this.deviationPct = deviationPct;
            this.unit = unit;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Double getResult() { return result; }
        public void setResult(Double result) { this.result = result; }

        public Double getDeviationPct() { return deviationPct; }
        public void setDeviationPct(Double deviationPct) { this.deviationPct = deviationPct; }

        public String getUnit() { return unit; }
        public void setUnit(String unit) { this.unit = unit; }
    }

    public static class ConditionRow {
        private String name;
        private Integer value;

        public ConditionRow() {}

        public ConditionRow(String name, Integer value) {
            this.name = name;
            this.value = value;
        }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public Integer getValue() { return value; }
        public void setValue(Integer value) { this.value = value; }
    }
}