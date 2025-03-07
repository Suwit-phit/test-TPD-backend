package com.example.matching.service;

import com.example.matching.model.PasswordResetToken;
import com.example.matching.model.User;
import com.example.matching.repository.PasswordResetTokenRepository;
import com.example.matching.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    // Generate a 6-digit numeric OTP
    public String createPasswordResetTokenForUser(User user) {
        String otp = generateNumericOTP(6);
        createOrUpdatePasswordResetTokenForUser(user, otp);
        return otp;
    }

    // Helper method to generate a numeric OTP of the specified length
    private String generateNumericOTP(int length) {
        Random random = new Random();
        int otp = 100000 + random.nextInt(900000); // Generates a random 6-digit number
        return String.valueOf(otp);
    }

    private void createOrUpdatePasswordResetTokenForUser(User user, String token) {
        PasswordResetToken existingToken = tokenRepository.findByUserId(user.getId());
        if (existingToken != null) {
            existingToken.setToken(token);
            existingToken.setExpiryDate(calculateExpiryDate());
            tokenRepository.save(existingToken);
        } else {
            PasswordResetToken passwordResetToken = new PasswordResetToken(token, user);
            passwordResetToken.setExpiryDate(calculateExpiryDate());
            tokenRepository.save(passwordResetToken);
        }
    }

    private Date calculateExpiryDate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.HOUR, 24); // Token expires in 24 hours
        return cal.getTime();
    }

    public PasswordResetToken validatePasswordResetToken(String token) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        return (passToken == null || passToken.getExpiryDate().before(new Date())) ? null : passToken;
    }

    public User getUserByPasswordResetToken(String token) {
        PasswordResetToken passToken = tokenRepository.findByToken(token);
        return passToken != null ? passToken.getUser() : null;
    }

    public void deleteToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }
}