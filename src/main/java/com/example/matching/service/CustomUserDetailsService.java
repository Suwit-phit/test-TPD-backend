//! Below is good and change to UUID
package com.example.matching.service;

import com.example.matching.model.User;
import com.example.matching.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.*;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByUsername(usernameOrEmail);
        if (userOptional.isPresent()) {
            return createSpringSecurityUser(userOptional.get());
        }

        userOptional = userRepository.findByEmail(usernameOrEmail);
        if (userOptional.isPresent()) {
            return createSpringSecurityUser(userOptional.get());
        }

        throw new UsernameNotFoundException("User not found with username or email CustomUserDetailsService: "
                + usernameOrEmail);
    }

    public UserDetails loadUserById(UUID userId) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            return createSpringSecurityUser(userOptional.get());
        }

        throw new UsernameNotFoundException("User not found with userId CustomUserDetailsService: " + userId);
    }

    private CustomUserDetails createSpringSecurityUser(User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        return new CustomUserDetails(user.getId(), user.getUsername(), user.getPassword(), authorities);
    }
}