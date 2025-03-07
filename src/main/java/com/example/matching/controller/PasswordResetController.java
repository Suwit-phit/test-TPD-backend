package com.example.matching.controller;

import com.example.matching.model.PasswordResetToken;
import com.example.matching.model.User;
import com.example.matching.service.EmailService;
import com.example.matching.service.PasswordResetTokenService;
import com.example.matching.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/password-reset")
public class PasswordResetController {

    @Autowired
    private PasswordResetTokenService passwordResetTokenService;

    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/request")
    public Map<String, String> requestPasswordReset(@RequestParam("email") String userEmail) {
        User user = userService.findUserByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("No user found with this email. From backend"));
        String token = passwordResetTokenService.createPasswordResetTokenForUser(user);
        emailService.sendPasswordResetEmail(userEmail, token);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password reset token has been sent to your email. \n Click next to validate token");
        return response;
    }

    @GetMapping("/validate")
    public Map<String, String> validateToken(@RequestParam("token") String token) {
        PasswordResetToken passToken = passwordResetTokenService.validatePasswordResetToken(token);
        if (passToken == null) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }
        Map<String, String> response = new HashMap<>();
        response.put("message", "Token is valid.");
        return response;
    }

    @PostMapping("/reset")
    public Map<String, String> resetPassword(@RequestParam("token") String token,
            @RequestParam("password") String newPassword) {
        PasswordResetToken passToken = passwordResetTokenService.validatePasswordResetToken(token);
        if (passToken == null) {
            throw new IllegalArgumentException("Invalid or expired token.");
        }
        User user = passToken.getUser();
        userService.updatePassword(user, newPassword);
        passwordResetTokenService.deleteToken(passToken);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Password has been reset successfully.");
        return response;
    }
}