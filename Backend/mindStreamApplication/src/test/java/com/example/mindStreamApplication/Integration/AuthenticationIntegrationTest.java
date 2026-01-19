package com.example.mindStreamApplication.Integration;

import com.example.mindStreamApplication.Domain.User;
import com.example.mindStreamApplication.Repository.UserRepository;
import com.example.mindStreamApplication.Service.AuthService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("Authentication Integration Tests")
class AuthenticationIntegrationTest {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Nested
    @DisplayName("Complete Registration Flow")
    class RegistrationFlowTests {

        @Test
        @DisplayName("Should complete full registration flow")
        void completeRegistrationFlow_ShouldWork() {
            // Arrange
            User newUser = new User("integrationuser", "integration@test.com", "password123", "Integration Test User");

            // Act
            Map<String, Object> result = authService.register(newUser);

            // Assert
            assertTrue((Boolean) result.get("success"));
            assertEquals("Registration successful", result.get("message"));

            // Verify user exists in database
            User savedUser = userRepository.findByUsername("integrationuser");
            assertNotNull(savedUser);
            assertEquals("integration@test.com", savedUser.getEmail());
            // Password should be encoded
            assertNotEquals("password123", savedUser.getPassword());
        }
    }

    @Nested
    @DisplayName("Complete Login Flow")
    class LoginFlowTests {

        @Test
        @DisplayName("Should complete full login flow after registration")
        void completeLoginFlowAfterRegistration_ShouldWork() {
            // Arrange - Register user first
            User newUser = new User("loginuser", "login@test.com", "password123", "Login Test User");
            authService.register(newUser);

            // Act - Login with registered user
            Map<String, Object> loginResult = authService.login("loginuser", "password123");

            // Assert
            assertTrue((Boolean) loginResult.get("success"));
            assertEquals("Login successful", loginResult.get("message"));
            assertNotNull(loginResult.get("token"));

            // Verify token is valid
            String token = (String) loginResult.get("token");
            assertTrue(authService.validateToken(token));
        }

        @Test
        @DisplayName("Should extract correct user ID from login token")
        void login_ShouldReturnTokenWithCorrectUserId() {
            // Arrange
            User newUser = new User("tokenuser", "token@test.com", "password123", "Token Test User");
            Map<String, Object> registerResult = authService.register(newUser);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> userMap = (Map<String, Object>) registerResult.get("user");
            Long expectedUserId = (Long) userMap.get("id");

            // Act
            Map<String, Object> loginResult = authService.login("tokenuser", "password123");
            String token = (String) loginResult.get("token");
            Long extractedUserId = authService.getUserIdFromToken(token);

            // Assert
            assertEquals(expectedUserId, extractedUserId);
        }
    }

    @Nested
    @DisplayName("User Retrieval Flow")
    class UserRetrievalTests {

        @Test
        @DisplayName("Should get user by ID after registration")
        void getUserById_AfterRegistration_ShouldWork() {
            // Arrange
            User newUser = new User("retrieveuser", "retrieve@test.com", "password123", "Retrieve Test User");
            Map<String, Object> registerResult = authService.register(newUser);
            
            @SuppressWarnings("unchecked")
            Map<String, Object> userMap = (Map<String, Object>) registerResult.get("user");
            Long userId = (Long) userMap.get("id");

            // Act
            User retrievedUser = authService.getUserById(userId);

            // Assert
            assertNotNull(retrievedUser);
            assertEquals("retrieveuser", retrievedUser.getUsername());
            assertEquals("retrieve@test.com", retrievedUser.getEmail());
        }
    }

    @Nested
    @DisplayName("Error Scenarios")
    class ErrorScenarioTests {

        @Test
        @DisplayName("Should fail login with wrong password")
        void login_WithWrongPassword_ShouldFail() {
            // Arrange
            User newUser = new User("wrongpassuser", "wrongpass@test.com", "correctPassword", "Wrong Pass User");
            authService.register(newUser);

            // Act & Assert
            assertThrows(Exception.class, () -> 
                authService.login("wrongpassuser", "wrongPassword")
            );
        }

        @Test
        @DisplayName("Should fail registration with duplicate username")
        void register_WithDuplicateUsername_ShouldFail() {
            // Arrange
            User user1 = new User("duplicateuser", "email1@test.com", "password", "User 1");
            User user2 = new User("duplicateuser", "email2@test.com", "password", "User 2");
            
            authService.register(user1);

            // Act & Assert
            assertThrows(Exception.class, () -> 
                authService.register(user2)
            );
        }

        @Test
        @DisplayName("Should fail registration with duplicate email")
        void register_WithDuplicateEmail_ShouldFail() {
            // Arrange
            User user1 = new User("user1", "duplicate@test.com", "password", "User 1");
            User user2 = new User("user2", "duplicate@test.com", "password", "User 2");
            
            authService.register(user1);

            // Act & Assert
            assertThrows(Exception.class, () -> 
                authService.register(user2)
            );
        }
    }
}
