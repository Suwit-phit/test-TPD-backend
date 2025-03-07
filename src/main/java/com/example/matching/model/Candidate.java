package com.example.matching.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "matchingCandidateFixCanVan")
@EntityListeners(AuditingEntityListener.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Candidate {

    @Id
    @GeneratedValue
    private UUID id;

    private String courtesyTitle;
    private String candidateName; // Changed from 'username' to 'candidateName'
    private String dateOfBirth;
    private String position;
    private double salary; // Changed from int to double
    private String employmentType;
    private String industry;
    private String phoneNumber;
    private String email;

    @ElementCollection
    @CollectionTable(name = "education_levelFixCanVan", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column(name = "education_level")
    private List<String> educationLevels;

    @ElementCollection
    @CollectionTable(name = "skillFixCanVan", joinColumns = @JoinColumn(name = "candidate_id"))
    @Column(name = "skill")
    private List<String> skills;

    @ElementCollection
    @CollectionTable(name = "contact_typeFixCanVan", joinColumns = @JoinColumn(name = "candidate_id"))
    private List<ContactType> contactTypes;

    @ElementCollection
    @CollectionTable(name = "attachmentFixCanVan", joinColumns = @JoinColumn(name = "candidate_id"))
    private List<Attachment> attachments;

    @ElementCollection
    @CollectionTable(name = "imageFixCanVan", joinColumns = @JoinColumn(name = "candidate_id"))
    private List<Image> images;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    //? For comment
    @OneToMany(mappedBy = "candidate")
    @JsonIgnore
    private List<Comment> comments;
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Attachment {
        private String fileName;

        @JsonIgnore
        @Lob
        private byte[] fileData;

        private LocalDateTime createdAt;

        public Attachment(String fileName, byte[] fileData) {
            this.fileName = fileName;
            this.fileData = fileData;
            this.createdAt = LocalDateTime.now(); // Set createdAt on creation
        }

        @PrePersist
        protected void onCreate() {
            this.createdAt = LocalDateTime.now();
        }

        @Override
        public String toString() {
            return fileName;
        }
    }

    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Image {
        private String fileName;

        @JsonIgnore
        @Lob
        private byte[] fileData;

        @Override
        public String toString() {
            return fileName;
        }
    }
}

// package com.example.matching.model;

// import jakarta.persistence.*;
// import java.time.LocalDateTime;
// import java.util.List;
// import java.util.UUID;
// import com.fasterxml.jackson.annotation.JsonIgnore;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;
// import lombok.Builder;
// import org.springframework.data.annotation.CreatedDate;
// import org.springframework.data.annotation.LastModifiedDate;
// import org.springframework.data.jpa.domain.support.AuditingEntityListener;

// @Entity
// @Table(name = "matchingCandidateFixCanVan")
// @EntityListeners(AuditingEntityListener.class)
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class Candidate {

//     @Id
//     @GeneratedValue
//     private UUID id;

//     private String courtesyTitle;
//     private String candidateName; // Changed from 'username' to 'candidateName'
//     private String dateOfBirth;
//     private String position;
//     private double salary; // Changed from int to double
//     private String employmentType;
//     private String industry;
//     private String phoneNumber;
//     private String email;

//     @ElementCollection
//     @CollectionTable(name = "education_level", joinColumns = @JoinColumn(name = "candidate_id"))
//     @Column(name = "education_level")
//     private List<String> educationLevels;

//     @ElementCollection
//     @CollectionTable(name = "skill", joinColumns = @JoinColumn(name = "candidate_id"))
//     @Column(name = "skill")
//     private List<String> skills;

//     @ElementCollection
//     @CollectionTable(name = "contact_type", joinColumns = @JoinColumn(name = "candidate_id"))
//     private List<ContactType> contactTypes;

//     @ElementCollection
//     @CollectionTable(name = "attachment", joinColumns = @JoinColumn(name = "candidate_id"))
//     private List<Attachment> attachments;

//     @ElementCollection
//     @CollectionTable(name = "image", joinColumns = @JoinColumn(name = "candidate_id"))
//     private List<Image> images;

//     @CreatedDate
//     @Column(nullable = false, updatable = false)
//     private LocalDateTime createdAt;

//     @LastModifiedDate
//     @Column(nullable = false)
//     private LocalDateTime updatedAt;

//     @Embeddable
//     @Data
//     @NoArgsConstructor
//     @AllArgsConstructor
//     public static class Attachment {
//         private String fileName;

//         @JsonIgnore
//         @Lob
//         private byte[] fileData;

//         private LocalDateTime createdAt;

//         public Attachment(String fileName, byte[] fileData) {
//             this.fileName = fileName;
//             this.fileData = fileData;
//             this.createdAt = LocalDateTime.now(); // Set createdAt on creation
//         }

//         @PrePersist
//         protected void onCreate() {
//             this.createdAt = LocalDateTime.now();
//         }

//         @Override
//         public String toString() {
//             return fileName;
//         }
//     }

//     @Embeddable
//     @Data
//     @NoArgsConstructor
//     @AllArgsConstructor
//     public static class Image {
//         private String fileName;

//         @JsonIgnore
//         @Lob
//         private byte[] fileData;

//         @Override
//         public String toString() {
//             return fileName;
//         }
//     }

//     // @Embeddable
//     // @Data
//     // @NoArgsConstructor
//     // @AllArgsConstructor
//     // public static class ContactType {
//     // private String type;

//     // @Override
//     // public String toString() {
//     // return type;
//     // }
//     // }
// }

// ! below code is good but not include every data
// package com.example.matching.model;

// import jakarta.persistence.*;
// import java.util.Date;

// import com.fasterxml.jackson.annotation.JsonIgnore;
// import lombok.Data;
// import lombok.NoArgsConstructor;
// import lombok.AllArgsConstructor;
// import lombok.Builder;

// @Entity
// @Table(name = "matchingCandidate")
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
// @Builder
// public class Candidate {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;

// private String name;
// private String position;
// private String skills;

// // Added to store the CV/Resume
// @Lob
// @JsonIgnore
// private byte[] resume;

// @Temporal(TemporalType.TIMESTAMP)
// private Date timestamp = new Date(); // Initialize timestamp
// }

// ! Below code is without Lombok
// package com.example.matching.model;

// import jakarta.persistence.*;
// import java.util.Date;

// import com.fasterxml.jackson.annotation.JsonIgnore;

// @Entity
// @Table(name = "matchingCandidate")
// public class Candidate {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;

// private String name;
// private String position;
// private String skills;

// // Added to store the CV/Resume
// @Lob
// @JsonIgnore
// private byte[] resume;

// @Temporal(TemporalType.TIMESTAMP)
// private Date timestamp;

// // Default constructor
// public Candidate() {
// this.timestamp = new Date(); // Initialize timestamp
// }

// // Constructor with parameters
// public Candidate(String name, String position, String skills, byte[] resume)
// {
// this.name = name;
// this.position = position;
// this.skills = skills;
// this.resume = resume;
// this.timestamp = new Date(); // Initialize timestamp
// }

// // Getters and setters for the new field
// public byte[] getResume() {
// return resume;
// }

// public void setResume(byte[] resume) {
// this.resume = resume;
// }

// public Long getId() {
// return id;
// }

// public void setId(Long id) {
// this.id = id;
// }

// public String getName() {
// return name;
// }

// public void setName(String name) {
// this.name = name;
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

// public Date getTimestamp() {
// return timestamp;
// }

// public void setTimestamp(Date timestamp) {
// this.timestamp = timestamp;
// }
// }

// ! Below code is good but not include with CvResume upload
// package com.example.matching.model;

// import jakarta.persistence.*;
// import java.util.Date;
// @Entity
// @Table(name = "matchingCandidate")
// public class Candidate {

// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;

// private String name;
// private String position;
// private String skills;

// @Temporal(TemporalType.TIMESTAMP)
// private Date timestamp;

// // Default constructor
// public Candidate() {
// this.timestamp = new Date(); // Initialize timestamp
// }

// // Constructor with parameters
// public Candidate(String name, String position, String skills) {
// this.name = name;
// this.position = position;
// this.skills = skills;
// this.timestamp = new Date(); // Initialize timestamp
// }

// public Long getId() {
// return id;
// }

// public void setId(Long id) {
// this.id = id;
// }

// public String getName() {
// return name;
// }

// public void setName(String name) {
// this.name = name;
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

// public Date getTimestamp() {
// return timestamp;
// }

// public void setTimestamp(Date timestamp) {
// this.timestamp = timestamp;
// }

// // Getters and setters (omitted for brevity)

// }

// ! Below code is good but not for the generate Excel
// package com.example.matching.model;

// import jakarta.persistence.*;
// import java.util.Date;

// @Entity
// @Table(name = "matchingCandidate")
// public class Candidate {
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;

// private String name;
// private String position;
// private String skills; // Skills separated by comma (e.g.,
// "React,Java,Python")

// // Add a timestamp field to track when the candidate was created or last
// updated
// private Date timestamp;

// // Constructors
// public Candidate() {
// // Default constructor
// }

// public Candidate(String name, String position, String skills) {
// this.name = name;
// this.position = position;
// this.skills = skills;
// this.timestamp = new Date(); // Set timestamp to current date and time
// }

// // Getters and setters
// public Long getId() {
// return id;
// }

// public void setId(Long id) {
// this.id = id;
// }

// public String getName() {
// return name;
// }

// public void setName(String name) {
// this.name = name;
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

// public Date getTimestamp() {
// return timestamp;
// }

// public void setTimestamp(Date timestamp) {
// this.timestamp = timestamp;
// }
// }

// ! End
// import jakarta.persistence.*;
// import java.util.Date;

// @Entity
// @Table(name = "matchingCandidate")
// public class Candidate {
// @Id
// @GeneratedValue(strategy = GenerationType.IDENTITY)
// private Long id;
// private String name;
// private String position;
// private String skills; // Skills separated by comma (e.g.,
// "React,Java,Python")

// // Add a timestamp field to track when the candidate was created or last
// updated
// private Date timestamp;

// // Getters and setters
// // Constructors
// // Other methods if needed

// public Long getId() {
// return id;
// }

// public void setId(Long id) {
// this.id = id;
// }

// public String getName() {
// return name;
// }

// public void setName(String name) {
// this.name = name;
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
