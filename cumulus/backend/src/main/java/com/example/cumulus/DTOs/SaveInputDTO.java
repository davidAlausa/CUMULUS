package com.example.cumulus.DTOs;

import java.util.List;
import java.util.Map;

public class SaveInputDTO {
    private List<QuestionDTO> questions;

    private Map<String, Integer> providerScore;
    private List<String> attributeIds;

    public List<QuestionDTO> getQuestions() {
        return questions;
    }

    public void setQuestions(List<QuestionDTO> questions) {
        this.questions = questions;
    }

    public Map<String, Integer> getProviderScore() {
        return providerScore;
    }

    public void setProviderScore(Map<String, Integer> providerScore) {
        this.providerScore = providerScore;
    }

    public List<String> getAttributeIds() {
        return attributeIds;
    }

    public void setAttributeIds(List<String> attributeIds) {
        this.attributeIds = attributeIds;
    }
}
