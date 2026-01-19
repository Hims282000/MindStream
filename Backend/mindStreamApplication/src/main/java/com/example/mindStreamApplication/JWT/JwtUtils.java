package com.example.mindStreamApplication.JWT;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JwtUtils {

    @Autowired
    private JwtService jwtService;

    // Extract token from Authorization header
    public String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }

    // Extract user ID from request
    public Long extractUserIdFromRequest(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && jwtService.validateToken(token)) {
            return jwtService.extractUserId(token);
        }
        return null;
    }

    // Extract username from request
    public String extractUsernameFromRequest(HttpServletRequest request) {
        String token = extractToken(request);
        if (token != null && jwtService.validateToken(token)) {
            return jwtService.extractUsername(token);
        }
        return null;
    }

    // Validate token from request
    public boolean validateTokenFromRequest(HttpServletRequest request) {
        String token = extractToken(request);
        return token != null && jwtService.validateToken(token);
    }
}
