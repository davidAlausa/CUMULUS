package com.example.cumulus.DTOs;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;


@Document(collection = "assessmentResults")
public class AssessmentResult {
    @Id
    private String id;
    private String userID;
    private Instant dateAssessed;
    private AssessmentModule assessmentModule;
    private CostEstimator costEstimator;
    private MigrationPlanner migrationPlanner;
    private Justification justification;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CostEstimator getCostEstimator() {
        return costEstimator;
    }

    public void setCostEstimator(CostEstimator costEstimator) {
        this.costEstimator = costEstimator;
    }

    public MigrationPlanner getMigrationPlanner() {
        return migrationPlanner;
    }

    public void setMigrationPlanner(MigrationPlanner migrationPlanner) {
        this.migrationPlanner = migrationPlanner;
    }

    public Instant getDateAssessed() {
        return dateAssessed;
    }

    public void setDateAssessed(Instant dateAssessed) {
        this.dateAssessed = dateAssessed;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public AssessmentModule getAssessmentModule() {
        return assessmentModule;
    }

    public void setAssessmentModule(AssessmentModule assessmentModule) {
        this.assessmentModule = assessmentModule;
    }

    public Justification getJustification() {
        return justification;
    }

    public void setJustification(Justification justification) {
        this.justification = justification;
    }
}
