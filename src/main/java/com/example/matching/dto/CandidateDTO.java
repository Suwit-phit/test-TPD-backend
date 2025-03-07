package com.example.matching.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidateDTO {
    private UUID id;
    private String courtesyTitle;
    private String CandidateName;
    // private String username;
    private String dateOfBirth;
    private String position;
    private double salary;
    private String employmentType;
    private String industry;
    private String phoneNumber;
    private String email;
    private List<String> educationLevels;
    private List<String> skills;

    private List<ContactTypeDTO> contactTypes;
    private List<AttachmentDTO> attachments;
    private List<ImageDTO> images;
}


// package com.example.matching.dto;

// import java.util.List;
// import java.util.UUID;

// public class CandidateDTO {
//     private UUID id;
//     private String courtesyTitle;
//     private String username;
//     private String dateOfBirth;
//     private String position;
//     private double salary;
//     private String employmentType;
//     private String industry;
//     private String phoneNumber;
//     private String email;
//     private List<String> educationLevels;
//     private List<String> skills;

//     private List<ContactTypeDTO> contactTypes;

//     private List<AttachmentDTO> attachments;

//     private List<ImageDTO> images;

//     public UUID getId() {
//         return id;
//     }

//     public void setId(UUID id) {
//         this.id = id;
//     }

//     public String getCourtesyTitle() {
//         return courtesyTitle;
//     }

//     public void setCourtesyTitle(String courtesyTitle) {
//         this.courtesyTitle = courtesyTitle;
//     }

//     public String getUsername() {
//         return username;
//     }

//     public void setUsername(String username) {
//         this.username = username;
//     }

//     public String getDateOfBirth() {
//         return dateOfBirth;
//     }

//     public void setDateOfBirth(String dateOfBirth) {
//         this.dateOfBirth = dateOfBirth;
//     }

//     public String getPosition() {
//         return position;
//     }

//     public void setPosition(String position) {
//         this.position = position;
//     }

//     public double getSalary() {
//         return salary;
//     }

//     public void setSalary(double salary) {
//         this.salary = salary;
//     }

//     public String getEmploymentType() {
//         return employmentType;
//     }

//     public void setEmploymentType(String employmentType) {
//         this.employmentType = employmentType;
//     }

//     public String getIndustry() {
//         return industry;
//     }

//     public void setIndustry(String industry) {
//         this.industry = industry;
//     }

//     public String getPhoneNumber() {
//         return phoneNumber;
//     }

//     public void setPhoneNumber(String phoneNumber) {
//         this.phoneNumber = phoneNumber;
//     }

//     public String getEmail() {
//         return email;
//     }

//     public void setEmail(String email) {
//         this.email = email;
//     }

//     public List<String> getEducationLevels() {
//         return educationLevels;
//     }

//     public void setEducationLevels(List<String> educationLevels) {
//         this.educationLevels = educationLevels;
//     }

//     public List<String> getSkills() {
//         return skills;
//     }

//     public void setSkills(List<String> skills) {
//         this.skills = skills;
//     }

//     // Getters and setters
//     public List<ContactTypeDTO> getContactTypes() {
//         return contactTypes;
//     }

//     public void setContactTypes(List<ContactTypeDTO> contactTypes) {
//         this.contactTypes = contactTypes;
//     }

//     public List<AttachmentDTO> getAttachments() {
//         return attachments;
//     }

//     public void setAttachments(List<AttachmentDTO> attachments) {
//         this.attachments = attachments;
//     }

//     public List<ImageDTO> getImages() {
//         return images;
//     }

//     public void setImages(List<ImageDTO> images) {
//         this.images = images;
//     }
// }
