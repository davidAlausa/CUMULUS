package com.example.cumulus.DTOs;

import com.example.cumulus.Models.UserProfile;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "refreshTokens")
public class RefreshToken {

    @Id
    private String id;
    private String token;
    private Instant expiryDate;
    private UserProfile user;

    public RefreshToken() {}

    public RefreshToken(String token, Instant expiryDate, UserProfile user) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public String getId() { return id; }
    public String getToken() { return token; }
    public Instant getExpiryDate() { return expiryDate; }
    public UserProfile getUser() { return user; }
}
