package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "procedure_check")
public class ProcedureCheck implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    @JsonBackReference
    private InspectionReport report;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private double result;

    @Column(name = "deviation_pct", nullable = false)
    private double deviationPct;

    @Column(length = 10)
    private String unit;

    public ProcedureCheck() {
    }

    public ProcedureCheck(String name, double result, double deviationPct, String unit) {
        this.name = name;
        this.result = result;
        this.deviationPct = deviationPct;
        this.unit = unit;
    }

    public Long getId() {
        return id;
    }

    public InspectionReport getReport() {
        return report;
    }

    public void setReport(InspectionReport report) {
        this.report = report;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }

    public double getDeviationPct() {
        return deviationPct;
    }

    public void setDeviationPct(double deviationPct) {
        this.deviationPct = deviationPct;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
