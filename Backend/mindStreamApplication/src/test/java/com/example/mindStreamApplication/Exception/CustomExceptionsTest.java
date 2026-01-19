package com.example.mindStreamApplication.Exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


@DisplayName("Custom Exception Tests")
class CustomExceptionsTest {

    @Nested
    @DisplayName("ResourceNotFoundException Tests")
    class ResourceNotFoundExceptionTests {

        @Test
        @DisplayName("Should create exception with simple message")
        void constructor_WithSimpleMessage_ShouldWork() {
            // Act
            ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

            // Assert
            assertEquals("Resource not found", exception.getMessage());
        }

        @Test
        @DisplayName("Should create exception with formatted message")
        void constructor_WithFormattedMessage_ShouldWork() {
            // Act
            ResourceNotFoundException exception = new ResourceNotFoundException("User", "id", 123L);

            // Assert
            assertTrue(exception.getMessage().contains("User"));
            assertTrue(exception.getMessage().contains("id"));
            assertTrue(exception.getMessage().contains("123"));
            assertTrue(exception.getMessage().contains("not found"));
        }

        @Test
        @DisplayName("Should extend RuntimeException")
        void shouldExtendRuntimeException() {
            // Act
            ResourceNotFoundException exception = new ResourceNotFoundException("Test");

            // Assert
            assertTrue(exception instanceof RuntimeException);
        }

        @Test
        @DisplayName("Should handle string field value")
        void constructor_WithStringFieldValue_ShouldWork() {
            // Act
            ResourceNotFoundException exception = new ResourceNotFoundException("User", "username", "john_doe");

            // Assert
            assertTrue(exception.getMessage().contains("john_doe"));
        }
    }

    @Nested
    @DisplayName("DuplicateResourceException Tests")
    class DuplicateResourceExceptionTests {

        @Test
        @DisplayName("Should create exception with simple message")
        void constructor_WithSimpleMessage_ShouldWork() {
            // Act
            DuplicateResourceException exception = new DuplicateResourceException("Duplicate entry");

            // Assert
            assertEquals("Duplicate entry", exception.getMessage());
        }

        @Test
        @DisplayName("Should create exception with formatted message")
        void constructor_WithFormattedMessage_ShouldWork() {
            // Act
            DuplicateResourceException exception = new DuplicateResourceException("User", "email", "test@email.com");

            // Assert
            assertTrue(exception.getMessage().contains("User"));
            assertTrue(exception.getMessage().contains("email"));
            assertTrue(exception.getMessage().contains("test@email.com"));
            assertTrue(exception.getMessage().contains("already exists"));
        }

        @Test
        @DisplayName("Should extend RuntimeException")
        void shouldExtendRuntimeException() {
            // Act
            DuplicateResourceException exception = new DuplicateResourceException("Test");

            // Assert
            assertTrue(exception instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("InvalidCredentialsException Tests")
    class InvalidCredentialsExceptionTests {

        @Test
        @DisplayName("Should create exception with message")
        void constructor_WithMessage_ShouldWork() {
            // Act
            InvalidCredentialsException exception = new InvalidCredentialsException("Invalid username or password");

            // Assert
            assertEquals("Invalid username or password", exception.getMessage());
        }

        @Test
        @DisplayName("Should extend RuntimeException")
        void shouldExtendRuntimeException() {
            // Act
            InvalidCredentialsException exception = new InvalidCredentialsException("Test");

            // Assert
            assertTrue(exception instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("UnauthorizedAccessException Tests")
    class UnauthorizedAccessExceptionTests {

        @Test
        @DisplayName("Should create exception with message")
        void constructor_WithMessage_ShouldWork() {
            // Act
            UnauthorizedAccessException exception = new UnauthorizedAccessException("Access denied");

            // Assert
            assertEquals("Access denied", exception.getMessage());
        }

        @Test
        @DisplayName("Should extend RuntimeException")
        void shouldExtendRuntimeException() {
            // Act
            UnauthorizedAccessException exception = new UnauthorizedAccessException("Test");

            // Assert
            assertTrue(exception instanceof RuntimeException);
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        @Test
        @DisplayName("Should handle null field value in ResourceNotFoundException")
        void resourceNotFoundException_WithNullValue_ShouldWork() {
            // Act
            ResourceNotFoundException exception = new ResourceNotFoundException("User", "id", null);

            // Assert
            assertTrue(exception.getMessage().contains("null"));
        }

        @Test
        @DisplayName("Should handle null field value in DuplicateResourceException")
        void duplicateResourceException_WithNullValue_ShouldWork() {
            // Act
            DuplicateResourceException exception = new DuplicateResourceException("User", "email", null);

            // Assert
            assertTrue(exception.getMessage().contains("null"));
        }

        @Test
        @DisplayName("Should handle empty string values")
        void exceptions_WithEmptyStrings_ShouldWork() {
            // Act
            ResourceNotFoundException exception1 = new ResourceNotFoundException("");
            DuplicateResourceException exception2 = new DuplicateResourceException("");
            InvalidCredentialsException exception3 = new InvalidCredentialsException("");
            UnauthorizedAccessException exception4 = new UnauthorizedAccessException("");

            // Assert
            assertEquals("", exception1.getMessage());
            assertEquals("", exception2.getMessage());
            assertEquals("", exception3.getMessage());
            assertEquals("", exception4.getMessage());
        }

        @Test
        @DisplayName("Should handle special characters in messages")
        void exceptions_WithSpecialCharacters_ShouldWork() {
            // Act
            String specialMessage = "Error: User 'john@test.com' not found! <script>alert('xss')</script>";
            ResourceNotFoundException exception = new ResourceNotFoundException(specialMessage);

            // Assert
            assertEquals(specialMessage, exception.getMessage());
        }

        @Test
        @DisplayName("Should handle very long messages")
        void exceptions_WithLongMessages_ShouldWork() {
            // Act
            String longMessage = "Error: ".repeat(1000);
            ResourceNotFoundException exception = new ResourceNotFoundException(longMessage);

            // Assert
            assertEquals(longMessage, exception.getMessage());
        }
    }
}
