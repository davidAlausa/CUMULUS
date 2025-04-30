package com.example.cumulus.DTOs;

import com.example.cumulus.Entities.Provider;
import com.example.cumulus.Entities.Resource;

import java.util.List;

public class MakeModuleDTO {
    private String providerId;
    private AssessmentDTO assessment;
    private List<Provider> providers;
    private List<Resource> resources;

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public AssessmentDTO getAssessment() {
        return assessment;
    }

    public void setAssessment(AssessmentDTO assessment) {
        this.assessment = assessment;
    }

    public List<Provider> getProviders() {
        return providers;
    }

    public void setProviders(List<Provider> providers) {
        this.providers = providers;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }
}
