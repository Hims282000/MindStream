package com.example.mindStreamApplication.JWT;

import com.example.mindStreamApplication.Domain.User;
import com.example.mindStreamApplication.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * Unit tests for JwtUserDetailsService
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JwtUserDetailsService Tests")
class JwtUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JwtUserDetailsService jwtUserDetailsService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "test@email.com", "encodedPassword", "Test User");
        testUser.setId(1L);
    }

    @Nested
    @DisplayName("LoadUserByUsername Tests")
    class LoadUserByUsernameTests {

        @Test
        @DisplayName("Should return UserDetails when user exists")
        void loadUserByUsername_WhenUserExists_ShouldReturnUserDetails() {
            // Arrange
            when(userRepository.findByUsername("testuser")).thenReturn(testUser);

            // Act
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("testuser");

            // Assert
            assertNotNull(userDetails);
            assertEquals("testuser", userDetails.getUsername());
            assertEquals("encodedPassword", userDetails.getPassword());
        }

        @Test
        @DisplayName("Should throw UsernameNotFoundException when user not exists")
        void loadUserByUsername_WhenUserNotExists_ShouldThrowException() {
            // Arrange
            when(userRepository.findByUsername("nonexistent")).thenReturn(null);

            // Act & Assert
            UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> jwtUserDetailsService.loadUserByUsername("nonexistent")
            );
            assertTrue(exception.getMessage().contains("User not found"));
            assertTrue(exception.getMessage().contains("nonexistent"));
        }

        @Test
        @DisplayName("Should return UserDetails with empty authorities")
        void loadUserByUsername_ShouldReturnUserWithEmptyAuthorities() {
            // Arrange
            when(userRepository.findByUsername("testuser")).thenReturn(testUser);

            // Act
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("testuser");

            // Assert
            assertNotNull(userDetails.getAuthorities());
            assertTrue(userDetails.getAuthorities().isEmpty());
        }

        @Test
        @DisplayName("Should handle special characters in username")
        void loadUserByUsername_WithSpecialCharacters_ShouldWork() {
            // Arrange
            User specialUser = new User("user@special#123", "special@email.com", "password", "Special User");
            when(userRepository.findByUsername("user@special#123")).thenReturn(specialUser);

            // Act
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("user@special#123");

            // Assert
            assertEquals("user@special#123", userDetails.getUsername());
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null username")
        void loadUserByUsername_WithNullUsername_ShouldThrowException() {
            // Arrange
            when(userRepository.findByUsername(null)).thenReturn(null);

            // Act & Assert
            assertThrows(
                UsernameNotFoundException.class,
                () -> jwtUserDetailsService.loadUserByUsername(null)
            );
        }

        @Test
        @DisplayName("Should handle empty username")
        void loadUserByUsername_WithEmptyUsername_ShouldThrowException() {
            // Arrange
            when(userRepository.findByUsername("")).thenReturn(null);

            // Act & Assert
            assertThrows(
                UsernameNotFoundException.class,
                () -> jwtUserDetailsService.loadUserByUsername("")
            );
        }

        @Test
        @DisplayName("Should preserve password encoding")
        void loadUserByUsername_ShouldPreserveEncodedPassword() {
            // Arrange
            testUser.setPassword("$2a$10$encodedPasswordHash");
            when(userRepository.findByUsername("testuser")).thenReturn(testUser);

            // Act
            UserDetails userDetails = jwtUserDetailsService.loadUserByUsername("testuser");

            // Assert
            assertEquals("$2a$10$encodedPasswordHash", userDetails.getPassword());
        }
    }
}
