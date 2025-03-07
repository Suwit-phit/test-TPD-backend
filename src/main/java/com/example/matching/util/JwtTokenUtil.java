package com.example.matching.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.*;

import com.example.matching.service.CustomUserDetails;

@Component
public class JwtTokenUtil {

    private String secret = "your_secret_key"; // Use a strong secret key and keep it secure

    // In-memory token blacklist (consider using Redis or a database in a real-world
    // application)
    private Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    // Retrieve username from jwt token
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    // Retrieve email from jwt token
    public String getEmailFromToken(String token) {
        return getAllClaimsFromToken(token).get("email", String.class);
    }

    // Retrieve user ID from jwt token
    // public UUID getUserIdFromToken(String token) {
    // return getAllClaimsFromToken(token).get("userId", UUID.class);
    // }
    // public UUID getUserIdFromToken(String token) {
    // String userId = getClaimFromToken(token, Claims::getSubject);
    // System.out.println("User ID extracted from token: " + userId);
    // return UUID.fromString(userId);
    // }
    public UUID getUserIdFromToken(String token) {
        String userIdString = getClaimFromToken(token, claims -> claims.get("userId", String.class)); // Retrieve as
        System.out.println("User ID extracted from token: " + userIdString); // String
        return UUID.fromString(userIdString); // Convert String back to UUID
    }

    // Retrieve expiration date from jwt token
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    // For retrieving any information from token we will need the secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody();
    }

    // Check if the token has expired
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    // Generate token for user
    // public String generateToken(UserDetails userDetails, String email, UUID
    // userId) {
    // Map<String, Object> claims = new HashMap<>();
    // claims.put("email", email);
    // claims.put("userId", userId);
    // return doGenerateToken(claims, userDetails.getUsername());
    // }
    // public String generateToken(UserDetails userDetails, String email, UUID
    // userId) {
    // Map<String, Object> claims = new HashMap<>();
    // claims.put("email", email);
    // claims.put("userId", userId.toString()); // Store UUID as String
    // return doGenerateToken(claims, userDetails.getUsername());
    // }

    // ? Generate token for user with only userId
    public String generateToken(UserDetails userDetails, UUID userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId.toString()); // Store UUID as String
        return doGenerateToken(claims, userId.toString()); // Use userId as the subject, or pass an empty string if you
                                                           // // don't need a subject
    }

    // While creating the token -
    // 1. Define claims of the token, like Issuer, Expiration, Subject, and the ID
    // 2. Sign the JWT using the HS512 algorithm and secret key.
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 2 * 60 * 60 * 1000))
                // .setExpiration(new Date(System.currentTimeMillis() + 30 * 1000))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    // Validate token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final UUID userIdFromToken = getUserIdFromToken(token);
        final UUID userIdFromUserDetails = ((CustomUserDetails) userDetails).getUserId();

        return (userIdFromToken.equals(userIdFromUserDetails) && !isTokenExpired(token) && !isTokenBlacklisted(token));
    }

    // Invalidate token
    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
    }

    // Check if token is blacklisted
    public boolean isTokenBlacklisted(String token) {
        return tokenBlacklist.contains(token);
    }
}