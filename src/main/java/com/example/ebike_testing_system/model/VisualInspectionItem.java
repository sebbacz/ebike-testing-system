package com.example.ebike_testing_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "visual_inspection_item")
public class VisualInspectionItem implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)  // Stores enum name (e.g., "VERY_GOOD")
    @Column(name = "state", length = 15, nullable = false)
    private ConditionState state;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", nullable = false)
    @JsonBackReference
    private InspectionReport report;

    @Column(length = 100, nullable = false)
    private String label;

//    @Column(length = 4, nullable = false)
//    private String state;

    public VisualInspectionItem() {
    }

    public VisualInspectionItem(String label, ConditionState state) {
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

    public void setId(Long id) {
        this.id = id;
    }

    public ConditionState getState() {
        return state;
    }

    public void setState(ConditionState state) {
        this.state = state;
    }


//    public String getState() {
//        return state;
//    }
//
//    public void setState(String state) {
//        this.state = state;
//    }
}