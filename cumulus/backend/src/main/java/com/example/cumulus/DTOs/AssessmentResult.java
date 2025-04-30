package com.example.cumulus.DTOs;

import com.example.cumulus.Entities.Resource;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;


public class AssessmentResult {

    private String CloudProvider;
    private String ProviderDescription;
    private CostEstimator costEstimator;
    private MigrationPlanner migrationPlanner;
    private String justification;


    public String getCloudProvider() {
        return CloudProvider;
    }

    public void setCloudProvider(String cloudProvider) {
        CloudProvider = cloudProvider;
    }

    public String getProviderDescription() {
        return ProviderDescription;
    }

    public void setProviderDescription(String providerDescription) {
        ProviderDescription = providerDescription;
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

    public String getJustification() {
        return justification;
    }

    public void setJustification(String justification) {
        this.justification = justification;
    }
}
