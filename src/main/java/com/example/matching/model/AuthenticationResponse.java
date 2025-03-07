package com.example.matching.model;

import java.util.*;;

public class AuthenticationResponse {
    private final String token;
    private final String username;
    private final String email;
    private final UUID userId; // Add this line

    public AuthenticationResponse(String token, String username, String email, UUID userId) {
        this.token = token;
        this.username = username;
        this.email = email;
        this.userId = userId; // Add this line
    }

    // Getters
    public String getToken() {
        return token;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public UUID getUserId() {
        return userId;
    }
}