package com.example.mindStreamApplication.Controller;

import com.example.mindStreamApplication.Domain.User;
import com.example.mindStreamApplication.Exception.DuplicateResourceException;
import com.example.mindStreamApplication.Exception.GlobalExceptionHandler;
import com.example.mindStreamApplication.Exception.InvalidCredentialsException;
import com.example.mindStreamApplication.Service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(AuthController.class)
@Import(GlobalExceptionHandler.class)
@DisplayName("AuthController Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    private User testUser;
    private Map<String, Object> successResponse;

    @BeforeEach
    void setUp() {
        testUser = new User("testuser", "test@email.com", "password123", "Test User");
        
        successResponse = new HashMap<>();
        successResponse.put("success", true);
        successResponse.put("message", "Operation successful");
        successResponse.put("user", Map.of(
            "id", 1L,
            "username", "testuser",
            "email", "test@email.com",
            "fullName", "Test User"
        ));
    }

    @Nested
    @DisplayName("POST /auth/register Tests")
    class RegisterTests {

        @Test
        @WithMockUser
        @DisplayName("Should register user successfully")
        void register_WithValidUser_ShouldReturnSuccess() throws Exception {
            // Arrange
            successResponse.put("message", "Registration successful");
            when(authService.register(any(User.class))).thenReturn(successResponse);

            // Act & Assert
            mockMvc.perform(post("/auth/register")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testUser)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Registration successful"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return conflict when username exists")
        void register_WithExistingUsername_ShouldReturnConflict() throws Exception {
            // Arrange
            when(authService.register(any(User.class)))
                    .thenThrow(new DuplicateResourceException("User", "username", "testuser"));

            // Act & Assert
            mockMvc.perform(post("/auth/register")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testUser)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return conflict when email exists")
        void register_WithExistingEmail_ShouldReturnConflict() throws Exception {
            // Arrange
            when(authService.register(any(User.class)))
                    .thenThrow(new DuplicateResourceException("User", "email", "test@email.com"));

            // Act & Assert
            mockMvc.perform(post("/auth/register")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(testUser)))
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    @Nested
    @DisplayName("POST /auth/login Tests")
    class LoginTests {

        @Test
        @WithMockUser
        @DisplayName("Should login successfully with valid credentials")
        void login_WithValidCredentials_ShouldReturnSuccess() throws Exception {
            // Arrange
            successResponse.put("message", "Login successful");
            successResponse.put("token", "jwt-token-here");
            when(authService.login("testuser", "password123")).thenReturn(successResponse);

            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("username", "testuser");
            loginRequest.put("password", "password123");

            // Act & Assert
            mockMvc.perform(post("/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Login successful"))
                    .andExpect(jsonPath("$.token").value("jwt-token-here"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return unauthorized with invalid credentials")
        void login_WithInvalidCredentials_ShouldReturnUnauthorized() throws Exception {
            // Arrange
            when(authService.login(anyString(), anyString()))
                    .thenThrow(new InvalidCredentialsException("Invalid username or password"));

            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("username", "wronguser");
            loginRequest.put("password", "wrongpassword");

            // Act & Assert
            mockMvc.perform(post("/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.success").value(false));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return bad request when username is missing")
        void login_WithMissingUsername_ShouldReturnBadRequest() throws Exception {
            // Arrange
            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("password", "password123");

            // Act & Assert
            mockMvc.perform(post("/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Username and password are required"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return bad request when password is missing")
        void login_WithMissingPassword_ShouldReturnBadRequest() throws Exception {
            // Arrange
            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("username", "testuser");

            // Act & Assert
            mockMvc.perform(post("/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Username and password are required"));
        }

        @Test
        @WithMockUser
        @DisplayName("Should return bad request when both fields are missing")
        void login_WithEmptyRequest_ShouldReturnBadRequest() throws Exception {
            // Arrange
            Map<String, String> loginRequest = new HashMap<>();

            // Act & Assert
            mockMvc.perform(post("/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @WithMockUser
        @DisplayName("Should handle special characters in username")
        void login_WithSpecialCharactersInUsername_ShouldWork() throws Exception {
            // Arrange
            successResponse.put("message", "Login successful");
            successResponse.put("token", "jwt-token");
            when(authService.login("user@special#123", "password")).thenReturn(successResponse);

            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("username", "user@special#123");
            loginRequest.put("password", "password");

            // Act & Assert
            mockMvc.perform(post("/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk());
        }

        @Test
        @WithMockUser
        @DisplayName("Should handle very long password")
        void login_WithVeryLongPassword_ShouldWork() throws Exception {
            // Arrange
            String longPassword = "a".repeat(1000);
            Map<String, String> loginRequest = new HashMap<>();
            loginRequest.put("username", "testuser");
            loginRequest.put("password", longPassword);

            successResponse.put("message", "Login successful");
            when(authService.login("testuser", longPassword)).thenReturn(successResponse);

            // Act & Assert
            mockMvc.perform(post("/auth/login")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(loginRequest)))
                    .andExpect(status().isOk());
        }
    }
}
