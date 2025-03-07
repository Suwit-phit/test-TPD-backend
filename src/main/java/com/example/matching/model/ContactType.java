package com.example.matching.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class ContactType {
    private String type;
    private String url;

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}

