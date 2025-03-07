package com.example.matching.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "attactments_outsideFixCanVan")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttactmentsOutside {

    @Id
    @GeneratedValue
    private UUID id;

    private String fileName;

    private String fileType;
    
    @Lob
    @JsonIgnore
    private byte[] data;
    // private byte[] data;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}

