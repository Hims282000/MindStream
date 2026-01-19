package com.example.mindStreamApplication.Domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("User Entity Tests")
class UserTest {

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
    }

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Default constructor should create empty User")
        void defaultConstructor_ShouldCreateEmptyUser() {
            User emptyUser = new User();
            
            assertNotNull(emptyUser);
            assertNull(emptyUser.getId());
            assertNull(emptyUser.getUsername());
            assertNull(emptyUser.getEmail());
            assertNull(emptyUser.getPassword());
            assertNull(emptyUser.getFullName());
        }

        @Test
        @DisplayName("Parameterized constructor should set all fields")
        void parameterizedConstructor_ShouldSetAllFields() {
            User newUser = new User("testuser", "test@email.com", "password123", "Test User");
            
            assertEquals("testuser", newUser.getUsername());
            assertEquals("test@email.com", newUser.getEmail());
            assertEquals("password123", newUser.getPassword());
            assertEquals("Test User", newUser.getFullName());
        }
    }

    @Nested
    @DisplayName("Getter and Setter Tests")
    class GetterSetterTests {

        @Test
        @DisplayName("Should set and get Id correctly")
        void shouldSetAndGetId() {
            user.setId(1L);
            assertEquals(1L, user.getId());
        }

        @Test
        @DisplayName("Should set and get Username correctly")
        void shouldSetAndGetUsername() {
            user.setUsername("john_doe");
            assertEquals("john_doe", user.getUsername());
        }

        @Test
        @DisplayName("Should set and get Email correctly")
        void shouldSetAndGetEmail() {
            user.setEmail("john@example.com");
            assertEquals("john@example.com", user.getEmail());
        }

        @Test
        @DisplayName("Should set and get Password correctly")
        void shouldSetAndGetPassword() {
            user.setPassword("securePassword123");
            assertEquals("securePassword123", user.getPassword());
        }

        @Test
        @DisplayName("Should set and get FullName correctly")
        void shouldSetAndGetFullName() {
            user.setFullName("John Doe");
            assertEquals("John Doe", user.getFullName());
        }
    }

    @Nested
    @DisplayName("ToString Tests")
    class ToStringTests {

        @Test
        @DisplayName("ToString should contain all user fields")
        void toString_ShouldContainAllFields() {
            user.setId(1L);
            user.setUsername("testuser");
            user.setEmail("test@email.com");
            user.setPassword("password");
            user.setFullName("Test User");

            String result = user.toString();

            assertTrue(result.contains("id=1"));
            assertTrue(result.contains("username='testuser'"));
            assertTrue(result.contains("email='test@email.com'"));
            assertTrue(result.contains("password='password'"));
            assertTrue(result.contains("fullName='Test User'"));
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null values gracefully")
        void shouldHandleNullValues() {
            user.setUsername(null);
            user.setEmail(null);
            user.setPassword(null);
            user.setFullName(null);

            assertNull(user.getUsername());
            assertNull(user.getEmail());
            assertNull(user.getPassword());
            assertNull(user.getFullName());
        }

        @Test
        @DisplayName("Should handle empty string values")
        void shouldHandleEmptyStrings() {
            user.setUsername("");
            user.setEmail("");
            user.setPassword("");
            user.setFullName("");

            assertEquals("", user.getUsername());
            assertEquals("", user.getEmail());
            assertEquals("", user.getPassword());
            assertEquals("", user.getFullName());
        }

        @Test
        @DisplayName("Should handle special characters in fields")
        void shouldHandleSpecialCharacters() {
            user.setUsername("user@#$%");
            user.setEmail("user+tag@domain.com");
            user.setFullName("José García-López");

            assertEquals("user@#$%", user.getUsername());
            assertEquals("user+tag@domain.com", user.getEmail());
            assertEquals("José García-López", user.getFullName());
        }
    }
}
