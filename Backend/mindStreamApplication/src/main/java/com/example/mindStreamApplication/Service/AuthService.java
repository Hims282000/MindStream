package com.example.mindStreamApplication.Service;

import com.example.mindStreamApplication.Domain.User;
import com.example.mindStreamApplication.Exception.DuplicateResourceException;
import com.example.mindStreamApplication.Exception.InvalidCredentialsException;
import com.example.mindStreamApplication.Exception.ResourceNotFoundException;
import com.example.mindStreamApplication.Repository.UserRepository;
import com.example.mindStreamApplication.JWT.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    // Register a new user
    public Map<String, Object> register(User user) {
        Map<String, Object> response = new HashMap<>();

        // Check if username already exists
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new DuplicateResourceException("User", "username", user.getUsername());
        }

        // Check if email already exists
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("User", "email", user.getEmail());
        }

        // Encode password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Save user to database
        User savedUser = userRepository.save(user);

        // Create response
        response.put("success", true);
        response.put("message", "Registration successful");
        
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", savedUser.getId());
        userMap.put("username", savedUser.getUsername());
        userMap.put("email", savedUser.getEmail());
        userMap.put("fullName", savedUser.getFullName() != null ? savedUser.getFullName() : "");
        response.put("user", userMap);

        return response;
    }

    // Login user - returns JWT token
    public Map<String, Object> login(String username, String password) {
        Map<String, Object> response = new HashMap<>();

        // Find user by username
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // Check password
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid username or password");
        }

        // Generate JWT token
        String token = jwtService.generateToken(user.getUsername(), user.getId());

        // Create response with token
        response.put("success", true);
        response.put("message", "Login successful");
        response.put("token", token);
        
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("id", user.getId());
        userMap.put("username", user.getUsername());
        userMap.put("email", user.getEmail());
        userMap.put("fullName", user.getFullName() != null ? user.getFullName() : "");
        response.put("user", userMap);

        return response;
    }

    // Get user by ID
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));
    }

    // Extract user ID from token
    public Long getUserIdFromToken(String token) {
        return jwtService.extractUserId(token);
    }

    // Validate token
    public boolean validateToken(String token) {
        return jwtService.validateToken(token);
    }
}