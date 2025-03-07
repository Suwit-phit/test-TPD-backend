//! SMTP brevo
package com.example.matching.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    public void sendPasswordResetEmail(String to, String token) {
        String subject = "Password Reset Request";
        String message = "Click the link to reset your password: " + token;
        // String message = "Click the link to reset your password:
        // https://your-app.com/reset?token=" + token;

        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        email.setFrom("sheerlit639@gmail.com");
        // email.setFrom("your_verified_email@domain.com");

        mailSender.send(email);
    }
}