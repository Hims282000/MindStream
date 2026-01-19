package com.example.mindStreamApplication.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthFilter Tests")
class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private UserDetails testUserDetails;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        testUserDetails = new User("testuser", "password", Collections.emptyList());
    }

    @Nested
    @DisplayName("DoFilterInternal Tests")
    class DoFilterInternalTests {

        @Test
        @DisplayName("Should continue filter chain when no Authorization header")
        void doFilterInternal_NoAuthHeader_ShouldContinueFilterChain() throws ServletException, IOException {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn(null);

            // Act
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        @DisplayName("Should continue filter chain when Authorization header doesn't start with Bearer")
        void doFilterInternal_NonBearerAuth_ShouldContinueFilterChain() throws ServletException, IOException {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

            // Act
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        @DisplayName("Should authenticate user when valid Bearer token provided")
        void doFilterInternal_ValidBearerToken_ShouldAuthenticateUser() throws ServletException, IOException {
            // Arrange
            String token = "valid.jwt.token";
            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(jwtService.extractUsername(token)).thenReturn("testuser");
            when(userDetailsService.loadUserByUsername("testuser")).thenReturn(testUserDetails);
            when(jwtService.validateToken(token)).thenReturn(true);

            // Act
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain).doFilter(request, response);
            assertNotNull(SecurityContextHolder.getContext().getAuthentication());
            assertEquals("testuser", SecurityContextHolder.getContext().getAuthentication().getName());
        }

        @Test
        @DisplayName("Should not authenticate when token is invalid")
        void doFilterInternal_InvalidToken_ShouldNotAuthenticate() throws ServletException, IOException {
            // Arrange
            String token = "invalid.jwt.token";
            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(jwtService.extractUsername(token)).thenReturn("testuser");
            when(userDetailsService.loadUserByUsername("testuser")).thenReturn(testUserDetails);
            when(jwtService.validateToken(token)).thenReturn(false);

            // Act
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
        }

        @Test
        @DisplayName("Should not authenticate when username is null")
        void doFilterInternal_NullUsername_ShouldNotAuthenticate() throws ServletException, IOException {
            // Arrange
            String token = "token.with.null.username";
            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(jwtService.extractUsername(token)).thenReturn(null);

            // Act
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain).doFilter(request, response);
            assertNull(SecurityContextHolder.getContext().getAuthentication());
            verify(userDetailsService, never()).loadUserByUsername(any());
        }

        @Test
        @DisplayName("Should not re-authenticate when already authenticated")
        void doFilterInternal_AlreadyAuthenticated_ShouldNotReAuthenticate() throws ServletException, IOException {
            // Arrange
            String token = "valid.jwt.token";
            when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
            when(jwtService.extractUsername(token)).thenReturn("testuser");
            when(userDetailsService.loadUserByUsername("testuser")).thenReturn(testUserDetails);
            when(jwtService.validateToken(token)).thenReturn(true);

            // First call to set authentication
            jwtAuthFilter.doFilterInternal(request, response, filterChain);
            
            // Reset mocks for second call
            reset(userDetailsService);

            // Act - Second call with existing authentication
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Assert - userDetailsService should not be called again (already authenticated)
            verify(userDetailsService, never()).loadUserByUsername(any());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle empty Bearer token")
        void doFilterInternal_EmptyBearerToken_ShouldHandleGracefully() throws ServletException, IOException {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn("Bearer ");
            
            // This might throw an exception when extracting username from empty token
            // Adjust based on actual implementation behavior
            try {
                when(jwtService.extractUsername("")).thenThrow(new RuntimeException("Invalid token"));
            } catch (Exception e) {
                // Expected
            }

            // Act
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should handle Bearer with extra spaces")
        void doFilterInternal_BearerWithExtraSpaces_ShouldWork() throws ServletException, IOException {
            // Arrange
            when(request.getHeader("Authorization")).thenReturn("Bearer   token.with.spaces");
            // Note: The actual token would be "  token.with.spaces" (with leading spaces)
            when(jwtService.extractUsername("  token.with.spaces")).thenReturn(null);

            // Act
            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain).doFilter(request, response);
        }
    }
}
