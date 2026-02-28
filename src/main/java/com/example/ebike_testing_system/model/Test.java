package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private UUID uuid;

    @Column(name = "ebike_id", insertable = false, updatable = false)
    private int ebikeId;
    @Enumerated(EnumType.STRING)
    private TestType type;

    @Enumerated(EnumType.STRING)
    private TestStatus status;

    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @Column(length = 1024)
    private String notes;

    @ManyToOne
    @JoinColumn(name = "ebike_id")
    private Ebike ebike;

    @ManyToOne
    @JoinColumn(name = "technician_id")
    private Technician technician;

    @ManyToOne
    @JoinColumn(name = "testbench_id")
    private TestBench testBench;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;


    @OneToMany(mappedBy = "test", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TestReport> reports = new ArrayList<>();

    private int batteryCapacity;
    private int maxSupport;
    private int enginePowerMax;
    private int enginePowerNominal;
    private int engineTorque;

    public Test(int id, UUID uuid, TestType type, TestStatus status,
                LocalDateTime startDate, LocalDateTime endDate, String notes,
                Ebike ebike, Technician technician, TestBench testBench, Customer customer,
                List<TestReport> reports,
                int batteryCapacity, int maxSupport, int enginePowerMax, int enginePowerNominal, int engineTorque) {
        this.id = id;
        this.uuid = uuid;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.ebike = ebike;
        this.technician = technician;
        this.testBench = testBench;
        this.customer = customer;
        this.reports = reports;
        this.batteryCapacity = batteryCapacity;
        this.maxSupport = maxSupport;
        this.enginePowerMax = enginePowerMax;
        this.enginePowerNominal = enginePowerNominal;
        this.engineTorque = engineTorque;
    }

    public Test() {}

//    public Test(int id, UUID uuid, TestType type,
//                int batteryCapacity, int maxSupport, TestStatus status,
//                int enginePowerMax, int enginePowerNominal, int engineTorque) {
//        this.id = id;
//        this.uuid = uuid;
//        this.type = type;
//        this.batteryCapacity = batteryCapacity;
//        this.maxSupport = maxSupport;
//        this.status = status;
//        this.enginePowerMax = enginePowerMax;
//        this.enginePowerNominal = enginePowerNominal;
//        this.engineTorque = engineTorque;
//    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public TestType getType() {
        return type;
    }

    public void setType(TestType type) {
        this.type = type;
    }

    public TestStatus getStatus() {
        return status;
    }

    public void setStatus(TestStatus status) {
        this.status = status;
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

    public List<TestReport> getReports() {
        return reports;
    }

    public void setReports(List<TestReport> reports) {
        this.reports = reports;
    }

    public void addReport(TestReport report) {
        reports.add(report);
        report.setTest(this);
    }

    public void removeReport(TestReport report) {
        reports.remove(report);
        report.setTest(null);
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Ebike getEbike() {
        return ebike;
    }

    public void setEbike(Ebike ebike) {
        this.ebike = ebike;
    }

    public Technician getTechnician() {
        return technician;
    }

    public void setTechnician(Technician technician) {
        this.technician = technician;
    }

    public TestBench getTestBench() {
        return testBench;
    }

    public void setTestBench(TestBench testBench) {
        this.testBench = testBench;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }


}
