package com.example.cumulus.DTOs;

import java.util.List;

public class ChatGPTRequest {
    private String model;
    private List<Message> messages;

    public ChatGPTRequest() {
    }

    public ChatGPTRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
