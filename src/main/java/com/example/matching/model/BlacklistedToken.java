// BlacklistedToken.java
package com.example.matching.model;

import jakarta.persistence.*;

import java.util.*;

@Entity
@Table(name = "BlacklistedToken_matching_UUID")
public class BlacklistedToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id; 

    private String token;

    public BlacklistedToken() {
    }

    public BlacklistedToken(String token) {
        this.token = token;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}