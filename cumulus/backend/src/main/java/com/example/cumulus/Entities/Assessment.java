package com.example.cumulus.Entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
import java.util.Map;

@Document(collection = "assessments")
public class Assessment {
    @Id
    private String id;
    private Instant dateCreated;
    private String userID;
    private Map<String, String> questionAnswer;
    private Map<String, Integer> providerScore;
    private List<String> attributeIds;
    private Map <String, String> providerResources;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Instant getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Instant dateCreated) {
        this.dateCreated = dateCreated;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public Map<String, String> getQuestionAnswer() {
        return questionAnswer;
    }

    public void setQuestionAnswer(Map<String, String> questionAnswer) {
        this.questionAnswer = questionAnswer;
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

    public Map<String, String> getProviderResources() {
        return providerResources;
    }

    public void setProviderResources(Map<String, String> providerResources) {
        this.providerResources = providerResources;
    }
}
