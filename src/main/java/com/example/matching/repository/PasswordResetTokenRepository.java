package com.example.matching.repository;

import com.example.matching.model.PasswordResetToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, UUID> { // Changed Long to UUID
    PasswordResetToken findByToken(String token);

    PasswordResetToken findByUserId(UUID userId); // Updated to UUID type
}