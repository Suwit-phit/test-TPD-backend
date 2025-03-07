package com.example.matching.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;
import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "users_matching_UUID")
@Data
@Builder // Enables the builder pattern
@NoArgsConstructor // Provides a no-argument constructor
@AllArgsConstructor // Provides an all-argument constructor
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id", columnDefinition = "UUID")
    private UUID id;

    @Column(name = "email")
    private String email;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles_UUID", 
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "UUID"))
    @Column(name = "role")
    private Set<String> roles;
}



//! without lombok
// package com.example.matching.model;

// import java.util.Set;
// import java.util.UUID;

// import jakarta.persistence.*;
// import org.hibernate.annotations.GenericGenerator;

// @Entity
// @Table(name = "users_matching_UUID")
// public class User {

//     @Id
//     @GeneratedValue
//     @Column(name = "id", columnDefinition = "UUID")
//     private UUID id;

//     // private String username;
//     // private String email;
//     @Column(name = "email")
//     private String email;

//     @Column(name = "username")
//     private String username;

//     @Column(name = "password")
//     private String password;

//     // Define roles in a custom table
//     @ElementCollection(fetch = FetchType.EAGER)
//     @CollectionTable(name = "user_roles_UUID", // Custom table name
//             joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id", columnDefinition = "UUID"))
//     @Column(name = "role") // Column name for the role values
//     private Set<String> roles;

//     // ! No custom table name
//     // @ElementCollection(fetch = FetchType.EAGER)
//     // private Set<String> roles;

//     // Getters and setters
//     public UUID getId() {
//         return id;
//     }

//     public void setId(UUID id) {
//         this.id = id;
//     }

//     public String getUsername() {
//         return username;
//     }

//     public void setUsername(String username) {
//         this.username = username;
//     }

//     public String getEmail() {
//         return email;
//     }

//     public void setEmail(String email) {
//         this.email = email;
//     }

//     public String getPassword() {
//         return password;
//     }

//     public void setPassword(String password) {
//         this.password = password;
//     }

//     public Set<String> getRoles() {
//         return roles;
//     }

//     public void setRoles(Set<String> roles) {
//         this.roles = roles;
//     }
// }