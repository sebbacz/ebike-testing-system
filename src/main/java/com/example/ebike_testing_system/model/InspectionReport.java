package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "inspection_report")
public class InspectionReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Link to the Test via UUID
    @Column(name = "test_uuid")
    private UUID testUuid; // NEW FIELD

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_bike_id", nullable = false)
    private CustomerBike customerBike;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(columnDefinition = "TEXT")
    private String comment;

    @Column(length = 100)
    private String brand;

    @Column(length = 100)
    private String model;

    @Column(name = "overall_score")
    private double overallScore;

    @Column(name = "battery_capacity_wh")
    private int batteryCapacityWh;

    @Column(name = "max_support_pct")
    private int maxSupportPct;

    @Column(name = "engine_power_max_w")
    private int enginePowerMaxW;

    @Column(name = "engine_power_nom_w")
    private int enginePowerNomW;

    @Column(name = "engine_torque_nm")
    private int engineTorqueNm;

    @Column(name = "test_proc_score")
    private double testProcScore;

    @Column(name = "nominal_load_cont_w")
    private int nominalLoadContW;

    @Column(name = "nominal_temp_rise_c_per_wh")
    private double nominalTempRiseCPerWh;

    @Column(name = "nominal_load_score")
    private double nominalLoadScore;

    @Column(name = "battery_test_capacity_wh")
    private int batteryTestCapacityWh;

    @Column(name = "battery_test_health_pct")
    private int batteryTestHealthPct;

    @Column(name = "battery_test_score")
    private double batteryTestScore;

    @Column(name = "bearing_score")
    private double bearingScore;

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<VisualInspectionItem> visualInspection = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<ProcedureCheck> testProcedures = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<BearingConditionItem> bearingConditionItems = new ArrayList<>();

    @OneToMany(mappedBy = "report", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<FunctionalCheck> functionalChecks = new ArrayList<>();

    public InspectionReport() {}

    // Getters and setters

    public Long getId() {
        return id;
    }

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

    public CustomerBike getCustomerBike() {
        return customerBike;
    }

    public void setCustomerBike(CustomerBike customerBike) {
        this.customerBike = customerBike;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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


    public int getBatteryCapacityWh() {
        return batteryCapacityWh;
    }

    public void setBatteryCapacityWh(int batteryCapacityWh) {
        this.batteryCapacityWh = batteryCapacityWh;
    }

    public int getMaxSupportPct() {
        return maxSupportPct;
    }

    public void setMaxSupportPct(int maxSupportPct) {
        this.maxSupportPct = maxSupportPct;
    }

    public int getEnginePowerMaxW() {
        return enginePowerMaxW;
    }

    public void setEnginePowerMaxW(int enginePowerMaxW) {
        this.enginePowerMaxW = enginePowerMaxW;
    }

    public int getEnginePowerNomW() {
        return enginePowerNomW;
    }

    public void setEnginePowerNomW(int enginePowerNomW) {
        this.enginePowerNomW = enginePowerNomW;
    }

    public int getEngineTorqueNm() {
        return engineTorqueNm;
    }

    public void setEngineTorqueNm(int engineTorqueNm) {
        this.engineTorqueNm = engineTorqueNm;
    }

    public double getTestProcScore() {
        return testProcScore;
    }

    public void setTestProcScore(double testProcScore) {
        this.testProcScore = testProcScore;
    }

    public int getNominalLoadContW() {
        return nominalLoadContW;
    }

    public void setNominalLoadContW(int nominalLoadContW) {
        this.nominalLoadContW = nominalLoadContW;
    }

    public double getNominalTempRiseCPerWh() {
        return nominalTempRiseCPerWh;
    }

    public void setNominalTempRiseCPerWh(double nominalTempRiseCPerWh) {
        this.nominalTempRiseCPerWh = nominalTempRiseCPerWh;
    }

    public double getNominalLoadScore() {
        return nominalLoadScore;
    }

    public void setNominalLoadScore(double nominalLoadScore) {
        this.nominalLoadScore = nominalLoadScore;
    }

    public int getBatteryTestCapacityWh() {
        return batteryTestCapacityWh;
    }

    public void setBatteryTestCapacityWh(int batteryTestCapacityWh) {
        this.batteryTestCapacityWh = batteryTestCapacityWh;
    }

    public int getBatteryTestHealthPct() {
        return batteryTestHealthPct;
    }

    public void setBatteryTestHealthPct(int batteryTestHealthPct) {
        this.batteryTestHealthPct = batteryTestHealthPct;
    }

    public double getBatteryTestScore() {
        return batteryTestScore;
    }

    public void setBatteryTestScore(double batteryTestScore) {
        this.batteryTestScore = batteryTestScore;
    }

    public double getBearingScore() {
        return bearingScore;
    }

    public void setBearingScore(double bearingScore) {
        this.bearingScore = bearingScore;
    }

    public List<VisualInspectionItem> getVisualInspection() {
        return visualInspection;
    }

    public List<ProcedureCheck> getTestProcedures() {
        return testProcedures;
    }

    public List<BearingConditionItem> getBearingConditionItems() {
        return bearingConditionItems;
    }

    public List<FunctionalCheck> getFunctionalChecks() {
        return functionalChecks;
    }
}