package com.example.cumulus.Authentications;

import lombok.Data;

@Data
public class authenticationRequest {
    private String email;
    private String password;
    public String getEmail() {
        return email;
    }
    public String getPassword() {
        return password;
    }
}
