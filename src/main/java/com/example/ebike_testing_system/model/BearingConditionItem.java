package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bearing_condition_item")
public class BearingConditionItem implements Serializable {

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
    private Integer value;

    public BearingConditionItem() {
    }

    public BearingConditionItem(String name, Integer value) {
        this.name = name;
        this.value = value;
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

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }
}