package com.example.matching.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "matching_vacanciesFixCanVan") // Updated table name
@Data // Lombok annotation for getters, setters, toString, equals, hashCode
@NoArgsConstructor // Lombok annotation for a no-argument constructor
public class Vacancy {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(updatable = false, nullable = false)
    private UUID id; // Use UUID as the primary key

    @Column(name = "position_title", nullable = false) // Changed from title to positionTitle
    private String positionTitle;

    @Column(name = "position_applied", nullable = false) // Mapping position applied
    private String positionApplied;

    @Column(name = "skills")
    private String skills;

    @Column(name = "salary")
    private Double salary; // Field for salary

    @Column(name = "currency_code", length = 6)
    private String currencyCode; // Field for currency code

    @Column(name = "date_of_application")
    private LocalDate dateOfApplication; // Field for the date of application

    @Column(updatable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Method to set creation and update times
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}



// ? Below code is super code but not include every data
// package com.example.matching.model;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;

// @Entity
// @Table(name = "matchingVacancies")
// public class Vacancy {
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;
// private String title;
// private String position;
// private String skills;

// private LocalDateTime createdAt;
// private LocalDateTime updatedAt;

// // Getters and setters
// // Constructors

// @PrePersist
// protected void onCreate() {
// createdAt = LocalDateTime.now();
// updatedAt = LocalDateTime.now();
// }

// @PreUpdate
// protected void onUpdate() {
// updatedAt = LocalDateTime.now();
// }

// public Long getId() {
// return id;
// }

// public void setId(Long id) {
// this.id = id;
// }

// public String getTitle() {
// return title;
// }

// public void setTitle(String title) {
// this.title = title;
// }

// public String getPosition() {
// return position;
// }

// public void setPosition(String position) {
// this.position = position;
// }

// public String getSkills() {
// return skills;
// }

// public void setSkills(String skills) {
// this.skills = skills;
// }

// public LocalDateTime getCreatedAt() {
// return createdAt;
// }

// public void setCreatedAt(LocalDateTime createdAt) {
// this.createdAt = createdAt;
// }

// public LocalDateTime getUpdatedAt() {
// return updatedAt;
// }

// public void setUpdatedAt(LocalDateTime updatedAt) {
// this.updatedAt = updatedAt;
// }
// }

// ! Below code is good
// package com.example.matching.model;

// import jakarta.persistence.*;

// @Entity
// @Table(name = "matchingVacancies")
// public class Vacancy {
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;
// private String title;
// private String position;
// private String skills; // Skills separated by comma (e.g.,
// "React,Java,Python")

// // Getters and setters
// // Constructors
// // Other methods if needed

// public Long getId() {
// return id;
// }

// public void setId(Long id) {
// this.id = id;
// }

// public String getTitle() {
// return title;
// }

// public void setTitle(String title) {
// this.title = title;
// }

// public String getPosition() {
// return position;
// }

// public void setPosition(String position) {
// this.position = position;
// }

// public String getSkills() {
// return skills;
// }

// public void setSkills(String skills) {
// this.skills = skills;
// }
// }
