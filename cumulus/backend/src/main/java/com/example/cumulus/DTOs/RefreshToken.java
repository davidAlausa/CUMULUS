package com.example.cumulus.DTOs;

import com.example.cumulus.DTOs.UserDetails;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "refreshTokens")
public class RefreshToken {

    @Id
    private String id; // Changed from Long to String
    private String token;
    private Instant expiryDate;
    private UserDetails user;

    public RefreshToken() {}

    public RefreshToken(String token, Instant expiryDate, UserDetails user) {
        this.token = token;
        this.expiryDate = expiryDate;
        this.user = user;
    }

    public String getId() { return id; } // Changed return type to String
    public String getToken() { return token; }
    public Instant getExpiryDate() { return expiryDate; }
    public UserDetails getUser() { return user; }
}
