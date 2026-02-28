package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "functional_check")
public class FunctionalCheck implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    @JsonBackReference
    private InspectionReport report;

    @Column(length = 100, nullable = false)
    private String label;

    @Column(length = 4, nullable = false)
    private ConditionState state;

    public FunctionalCheck() {
    }

    public FunctionalCheck(String label, ConditionState state) {
        this.label = label;
        this.state = state;
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

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ConditionState getState() {
        return state;
    }

    public void setState(ConditionState state) {
        this.state = state;
    }
}