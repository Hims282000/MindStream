package com.example.mindStreamApplication.Service;

import com.example.mindStreamApplication.Domain.User;
import com.example.mindStreamApplication.Exception.DuplicateResourceException;
import com.example.mindStreamApplication.Exception.InvalidCredentialsException;
import com.example.mindStreamApplication.Exception.ResourceNotFoundException;
import com.example.mindStreamApplication.JWT.JwtService;
import com.example.mindStreamApplication.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService Tests")
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthService authService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "test@email.com", "password123", "Test User");
        testUser.setId(1L);
    }

    @Nested
    @DisplayName("Register Tests")
    class RegisterTests {

        @Test
        @DisplayName("Should register new user successfully")
        void register_WhenValidUser_ShouldReturnSuccessResponse() {
            // Arrange
            User newUser = new User("newuser", "new@email.com", "password", "New User");
            when(userRepository.existsByUsername("newuser")).thenReturn(false);
            when(userRepository.existsByEmail("new@email.com")).thenReturn(false);
            when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User saved = invocation.getArgument(0);
                saved.setId(1L);
                return saved;
            });

            // Act
            Map<String, Object> result = authService.register(newUser);

            // Assert
            assertTrue((Boolean) result.get("success"));
            assertEquals("Registration successful", result.get("message"));
            assertNotNull(result.get("user"));
            verify(userRepository).save(any(User.class));
            verify(passwordEncoder).encode("password");
        }

        @Test
        @DisplayName("Should throw exception when username already exists")
        void register_WhenUsernameExists_ShouldThrowException() {
            // Arrange
            when(userRepository.existsByUsername("testuser")).thenReturn(true);

            // Act & Assert
            DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> authService.register(testUser)
            );
            assertTrue(exception.getMessage().contains("username"));
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void register_WhenEmailExists_ShouldThrowException() {
            // Arrange
            when(userRepository.existsByUsername("testuser")).thenReturn(false);
            when(userRepository.existsByEmail("test@email.com")).thenReturn(true);

            // Act & Assert
            DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> authService.register(testUser)
            );
            assertTrue(exception.getMessage().contains("email"));
            verify(userRepository, never()).save(any());
        }

        @Test
        @DisplayName("Should encode password before saving")
        void register_ShouldEncodePassword() {
            // Arrange
            User newUser = new User("newuser", "new@email.com", "plainPassword", "New User");
            when(userRepository.existsByUsername(anyString())).thenReturn(false);
            when(userRepository.existsByEmail(anyString())).thenReturn(false);
            when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
            when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
                User saved = invocation.getArgument(0);
                saved.setId(1L);
                return saved;
            });

            // Act
            authService.register(newUser);

            // Assert
            verify(passwordEncoder).encode("plainPassword");
        }
    }

    @Nested
    @DisplayName("Login Tests")
    class LoginTests {

        @Test
        @DisplayName("Should login successfully with valid credentials")
        void login_WithValidCredentials_ShouldReturnSuccessResponse() {
            // Arrange
            testUser.setPassword("encodedPassword");
            when(userRepository.findByUsername("testuser")).thenReturn(testUser);
            when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
            when(jwtService.generateToken("testuser", 1L)).thenReturn("jwt-token");

            // Act
            Map<String, Object> result = authService.login("testuser", "password123");

            // Assert
            assertTrue((Boolean) result.get("success"));
            assertEquals("Login successful", result.get("message"));
            assertEquals("jwt-token", result.get("token"));
            assertNotNull(result.get("user"));
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void login_WhenUserNotFound_ShouldThrowException() {
            // Arrange
            when(userRepository.findByUsername("nonexistent")).thenReturn(null);

            // Act & Assert
            InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login("nonexistent", "password")
            );
            assertEquals("Invalid username or password", exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception when password is wrong")
        void login_WhenPasswordWrong_ShouldThrowException() {
            // Arrange
            testUser.setPassword("encodedPassword");
            when(userRepository.findByUsername("testuser")).thenReturn(testUser);
            when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

            // Act & Assert
            InvalidCredentialsException exception = assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login("testuser", "wrongPassword")
            );
            assertEquals("Invalid username or password", exception.getMessage());
        }

        @Test
        @DisplayName("Should generate JWT token on successful login")
        void login_ShouldGenerateJwtToken() {
            // Arrange
            testUser.setPassword("encodedPassword");
            when(userRepository.findByUsername("testuser")).thenReturn(testUser);
            when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
            when(jwtService.generateToken("testuser", 1L)).thenReturn("jwt-token");

            // Act
            authService.login("testuser", "password");

            // Assert
            verify(jwtService).generateToken("testuser", 1L);
        }
    }

    @Nested
    @DisplayName("GetUserById Tests")
    class GetUserByIdTests {

        @Test
        @DisplayName("Should return user when found")
        void getUserById_WhenUserExists_ShouldReturnUser() {
            // Arrange
            when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

            // Act
            User result = authService.getUserById(1L);

            // Assert
            assertNotNull(result);
            assertEquals("testuser", result.getUsername());
        }

        @Test
        @DisplayName("Should throw exception when user not found")
        void getUserById_WhenUserNotFound_ShouldThrowException() {
            // Arrange
            when(userRepository.findById(999L)).thenReturn(Optional.empty());

            // Act & Assert
            assertThrows(
                ResourceNotFoundException.class,
                () -> authService.getUserById(999L)
            );
        }
    }

    @Nested
    @DisplayName("Token Operations Tests")
    class TokenOperationsTests {

        @Test
        @DisplayName("Should extract user ID from token")
        void getUserIdFromToken_ShouldReturnUserId() {
            // Arrange
            when(jwtService.extractUserId("test-token")).thenReturn(1L);

            // Act
            Long userId = authService.getUserIdFromToken("test-token");

            // Assert
            assertEquals(1L, userId);
        }

        @Test
        @DisplayName("Should validate token successfully")
        void validateToken_WhenValid_ShouldReturnTrue() {
            // Arrange
            when(jwtService.validateToken("valid-token")).thenReturn(true);

            // Act
            boolean result = authService.validateToken("valid-token");

            // Assert
            assertTrue(result);
        }

        @Test
        @DisplayName("Should return false for invalid token")
        void validateToken_WhenInvalid_ShouldReturnFalse() {
            // Arrange
            when(jwtService.validateToken("invalid-token")).thenReturn(false);

            // Act
            boolean result = authService.validateToken("invalid-token");

            // Assert
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null username in login")
        void login_WithNullUsername_ShouldThrowException() {
            // Arrange
            when(userRepository.findByUsername(null)).thenReturn(null);

            // Act & Assert
            assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login(null, "password")
            );
        }

        @Test
        @DisplayName("Should handle empty password in login")
        void login_WithEmptyPassword_ShouldThrowException() {
            // Arrange
            testUser.setPassword("encodedPassword");
            when(userRepository.findByUsername("testuser")).thenReturn(testUser);
            when(passwordEncoder.matches("", "encodedPassword")).thenReturn(false);

            // Act & Assert
            assertThrows(
                InvalidCredentialsException.class,
                () -> authService.login("testuser", "")
            );
        }
    }
}
