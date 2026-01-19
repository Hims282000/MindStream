package com.example.mindStreamApplication.JWT;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("JwtService Tests")
class JwtServiceTest {

    private JwtService jwtService;

    private static final String TEST_SECRET = "mySecretKeyForTesting12345678901234567890123456789012345678901234";
    private static final long TEST_EXPIRATION = 86400000L; // 24 hours

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        ReflectionTestUtils.setField(jwtService, "jwtSecret", TEST_SECRET);
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", TEST_EXPIRATION);
    }

    @Nested
    @DisplayName("GenerateToken Tests")
    class GenerateTokenTests {

        @Test
        @DisplayName("Should generate valid JWT token")
        void generateToken_ShouldReturnValidToken() {
            // Act
            String token = jwtService.generateToken("testuser", 1L);

            // Assert
            assertNotNull(token);
            assertFalse(token.isEmpty());
            assertTrue(token.split("\\.").length == 3); // JWT has 3 parts
        }

        @Test
        @DisplayName("Should generate different tokens for different users")
        void generateToken_DifferentUsers_ShouldGenerateDifferentTokens() {
            // Act
            String token1 = jwtService.generateToken("user1", 1L);
            String token2 = jwtService.generateToken("user2", 2L);

            // Assert
            assertNotEquals(token1, token2);
        }

        @Test
        @DisplayName("Should include userId in token")
        void generateToken_ShouldIncludeUserId() {
            // Act
            String token = jwtService.generateToken("testuser", 123L);
            Long extractedUserId = jwtService.extractUserId(token);

            // Assert
            assertEquals(123L, extractedUserId);
        }
    }

    @Nested
    @DisplayName("ExtractUsername Tests")
    class ExtractUsernameTests {

        @Test
        @DisplayName("Should extract username from token")
        void extractUsername_ShouldReturnCorrectUsername() {
            // Arrange
            String token = jwtService.generateToken("testuser", 1L);

            // Act
            String username = jwtService.extractUsername(token);

            // Assert
            assertEquals("testuser", username);
        }

        @Test
        @DisplayName("Should handle special characters in username")
        void extractUsername_WithSpecialCharacters_ShouldWork() {
            // Arrange
            String token = jwtService.generateToken("user@special#123", 1L);

            // Act
            String username = jwtService.extractUsername(token);

            // Assert
            assertEquals("user@special#123", username);
        }
    }

    @Nested
    @DisplayName("ExtractUserId Tests")
    class ExtractUserIdTests {

        @Test
        @DisplayName("Should extract userId from token")
        void extractUserId_ShouldReturnCorrectUserId() {
            // Arrange
            String token = jwtService.generateToken("testuser", 456L);

            // Act
            Long userId = jwtService.extractUserId(token);

            // Assert
            assertEquals(456L, userId);
        }

        @Test
        @DisplayName("Should handle large userId")
        void extractUserId_WithLargeId_ShouldWork() {
            // Arrange
            Long largeId = Long.MAX_VALUE;
            String token = jwtService.generateToken("testuser", largeId);

            // Act
            Long userId = jwtService.extractUserId(token);

            // Assert
            assertEquals(largeId, userId);
        }
    }

    @Nested
    @DisplayName("ExtractExpiration Tests")
    class ExtractExpirationTests {

        @Test
        @DisplayName("Should extract expiration date from token")
        void extractExpiration_ShouldReturnFutureDate() {
            // Arrange
            String token = jwtService.generateToken("testuser", 1L);

            // Act
            Date expiration = jwtService.extractExpiration(token);

            // Assert
            assertNotNull(expiration);
            assertTrue(expiration.after(new Date()));
        }

        @Test
        @DisplayName("Expiration should be after current time plus expiration period")
        void extractExpiration_ShouldBeWithinExpectedRange() {
            // Arrange
            long beforeGeneration = System.currentTimeMillis();
            String token = jwtService.generateToken("testuser", 1L);
            long afterGeneration = System.currentTimeMillis();

            // Act
            Date expiration = jwtService.extractExpiration(token);

            // Assert
            long expectedMinExpiration = beforeGeneration + TEST_EXPIRATION;
            long expectedMaxExpiration = afterGeneration + TEST_EXPIRATION + 1000; // 1 second buffer

            assertTrue(expiration.getTime() >= expectedMinExpiration);
            assertTrue(expiration.getTime() <= expectedMaxExpiration);
        }
    }

    @Nested
    @DisplayName("ValidateToken Tests")
    class ValidateTokenTests {

        @Test
        @DisplayName("Should return true for valid token")
        void validateToken_WithValidToken_ShouldReturnTrue() {
            // Arrange
            String token = jwtService.generateToken("testuser", 1L);

            // Act
            Boolean isValid = jwtService.validateToken(token);

            // Assert
            assertTrue(isValid);
        }

        @Test
        @DisplayName("Should return false for invalid token")
        void validateToken_WithInvalidToken_ShouldReturnFalse() {
            // Act
            Boolean isValid = jwtService.validateToken("invalid.token.here");

            // Assert
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should return false for malformed token")
        void validateToken_WithMalformedToken_ShouldReturnFalse() {
            // Act
            Boolean isValid = jwtService.validateToken("not-a-jwt");

            // Assert
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should return false for empty token")
        void validateToken_WithEmptyToken_ShouldReturnFalse() {
            // Act
            Boolean isValid = jwtService.validateToken("");

            // Assert
            assertFalse(isValid);
        }

        @Test
        @DisplayName("Should return false for expired token")
        void validateToken_WithExpiredToken_ShouldReturnFalse() {
            // Arrange - Create service with very short expiration
            JwtService shortExpService = new JwtService();
            ReflectionTestUtils.setField(shortExpService, "jwtSecret", TEST_SECRET);
            ReflectionTestUtils.setField(shortExpService, "jwtExpiration", 1L); // 1ms expiration

            String token = shortExpService.generateToken("testuser", 1L);

            // Wait for token to expire
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Act
            Boolean isValid = shortExpService.validateToken(token);

            // Assert
            assertFalse(isValid);
        }
    }

    @Nested
    @DisplayName("GetTokenFromHeader Tests")
    class GetTokenFromHeaderTests {

        @Test
        @DisplayName("Should extract token from Bearer header")
        void getTokenFromHeader_WithValidBearerHeader_ShouldReturnToken() {
            // Arrange
            String authHeader = "Bearer eyJhbGciOiJIUzI1NiJ9.test.signature";

            // Act
            String token = jwtService.getTokenFromHeader(authHeader);

            // Assert
            assertEquals("eyJhbGciOiJIUzI1NiJ9.test.signature", token);
        }

        @Test
        @DisplayName("Should return null for null header")
        void getTokenFromHeader_WithNullHeader_ShouldReturnNull() {
            // Act
            String token = jwtService.getTokenFromHeader(null);

            // Assert
            assertNull(token);
        }

        @Test
        @DisplayName("Should return null for header without Bearer prefix")
        void getTokenFromHeader_WithoutBearerPrefix_ShouldReturnNull() {
            // Arrange
            String authHeader = "Basic username:password";

            // Act
            String token = jwtService.getTokenFromHeader(authHeader);

            // Assert
            assertNull(token);
        }

        @Test
        @DisplayName("Should return null for empty header")
        void getTokenFromHeader_WithEmptyHeader_ShouldReturnNull() {
            // Act
            String token = jwtService.getTokenFromHeader("");

            // Assert
            assertNull(token);
        }

        @Test
        @DisplayName("Should handle Bearer with only spaces")
        void getTokenFromHeader_WithBearerAndSpaces_ShouldReturnEmptyString() {
            // Arrange
            String authHeader = "Bearer ";

            // Act
            String token = jwtService.getTokenFromHeader(authHeader);

            // Assert
            assertEquals("", token);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null username in token generation")
        void generateToken_WithNullUsername_ShouldWork() {
            // This test verifies behavior with null - may throw or handle gracefully
            // Adjust based on expected behavior
            String token = jwtService.generateToken(null, 1L);
            
            // Should still generate a token
            assertNotNull(token);
        }

        @Test
        @DisplayName("Should handle unicode characters in username")
        void generateToken_WithUnicodeUsername_ShouldWork() {
            // Arrange
            String unicodeUsername = "用户名测试";

            // Act
            String token = jwtService.generateToken(unicodeUsername, 1L);
            String extractedUsername = jwtService.extractUsername(token);

            // Assert
            assertEquals(unicodeUsername, extractedUsername);
        }

        @Test
        @DisplayName("Should handle very long username")
        void generateToken_WithVeryLongUsername_ShouldWork() {
            // Arrange
            String longUsername = "a".repeat(1000);

            // Act
            String token = jwtService.generateToken(longUsername, 1L);
            String extractedUsername = jwtService.extractUsername(token);

            // Assert
            assertEquals(longUsername, extractedUsername);
        }
    }
}
