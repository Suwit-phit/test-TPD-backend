package com.example.matching.controller;

import com.example.matching.exception.ForbiddenException;
import com.example.matching.exception.UnauthorizedException;
import com.example.matching.model.AuthenticationRequest;
import com.example.matching.model.AuthenticationResponse;
import com.example.matching.model.UpdateResponse;
import com.example.matching.model.User;
import com.example.matching.repository.UserRepository;
import com.example.matching.service.CustomUserDetailsService;
import com.example.matching.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsernameOrEmail(),
                            authenticationRequest.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsernameOrEmail());
            User user = userRepository.findByUsernameOrEmail(authenticationRequest.getUsernameOrEmail())
                    .orElseThrow(() -> new BadCredentialsException(
                            "User not found with username or email: " + authenticationRequest.getUsernameOrEmail()));

            // String jwt = jwtTokenUtil.generateToken(userDetails, user.getEmail(),
            // user.getId().toString());
            // String jwt = jwtTokenUtil.generateToken(userDetails, user.getEmail(), user.getId());
            
            // ? Generate token for user with only userId
            String jwt = jwtTokenUtil.generateToken(userDetails, user.getId());

            return ResponseEntity
                    .ok(new AuthenticationResponse(jwt, user.getUsername(), user.getEmail(), user.getId()));
        } catch (BadCredentialsException e) {
            return handleBadCredentialsException(authenticationRequest);
        }
    }

    private ResponseEntity<String> handleBadCredentialsException(AuthenticationRequest authenticationRequest) {
        Optional<User> userOptional = userRepository.findByUsername(authenticationRequest.getUsernameOrEmail());
        if (userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Your input password is not correct");
        }

        userOptional = userRepository.findByEmail(authenticationRequest.getUsernameOrEmail());
        if (userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Your input password is not correct");
        }

        userOptional = userRepository.findByUsername(authenticationRequest.getUsernameOrEmail());
        if (userOptional.isPresent()) {
            return ResponseEntity.badRequest().body("Your input username is not correct");
        }

        if (authenticationRequest.getUsernameOrEmail().contains("@")) {
            return ResponseEntity.badRequest().body("You input email incorrect");
        } else {
            return ResponseEntity.badRequest().body("Input username incorrect");
        }
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<?> updateUser(@PathVariable UUID id, @RequestBody User updatedUser) {
        System.out.println("Enter the  @PutMapping(\"/user/{id}\")");
        // Check authentication
        if (!isAuthenticated()) {
            throw new UnauthorizedException("You are not authorized to perform this operation.");
        }

        // Check update permission
        if (!hasUpdatePermission(id)) {
            throw new ForbiddenException("You are not allowed to update this user.");
        }

        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isEmpty()) {
            return ResponseEntity.badRequest().body("User not found");
        }

        User user = userOptional.get();

        boolean usernameChanged = !user.getUsername().equals(updatedUser.getUsername());
        boolean emailChanged = !user.getEmail().equals(updatedUser.getEmail());

        user.setUsername(updatedUser.getUsername());
        user.setEmail(updatedUser.getEmail());
        userRepository.save(user);

        String responseMessage;
        if (usernameChanged && emailChanged) {
            System.out.println("Enter  if (usernameChanged && emailChanged)");
            responseMessage = "Username and email updated successfully.";
        } else if (usernameChanged) {
            System.out.println("Enter  } else if (usernameChanged) {");
            responseMessage = "Username updated successfully.";
        } else if (emailChanged) {
            responseMessage = "Email updated successfully.";
        } else {
            responseMessage = "No changes detected.";
        }
        return ResponseEntity.ok()
                .body(new UpdateResponse(responseMessage, user.getUsername(), user.getEmail(), user.getId()));
    }

    // Placeholder for authentication check
    private boolean isAuthenticated() {
        // Implement your authentication logic here
        return true; // Placeholder
    }

    // Placeholder for permission check
    private boolean hasUpdatePermission(UUID id) {
        // Implement your permission check logic here
        return true; // Placeholder
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID id) {
        Optional<User> userOptional = userRepository.findById(id);
        if (userOptional.isPresent()) {
            userRepository.delete(userOptional.get());
            return ResponseEntity.ok("User deleted successfully");
        } else {
            return ResponseEntity.badRequest().body("User not found");
        }
    }

}