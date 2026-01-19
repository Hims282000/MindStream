package com.example.mindStreamApplication.Exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@DisplayName("GlobalExceptionHandler Tests")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Nested
    @DisplayName("ResourceNotFoundException Handler Tests")
    class ResourceNotFoundExceptionTests {

        @Test
        @DisplayName("Should return NOT_FOUND status with correct response")
        void handleResourceNotFoundException_ShouldReturnNotFound() {
            // Arrange
            ResourceNotFoundException exception = new ResourceNotFoundException("User", "id", 123L);

            // Act
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleResourceNotFoundException(exception);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse((Boolean) response.getBody().get("success"));
            assertTrue(response.getBody().get("message").toString().contains("User"));
            assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().get("status"));
            assertNotNull(response.getBody().get("timestamp"));
        }

        @Test
        @DisplayName("Should handle simple message constructor")
        void handleResourceNotFoundException_WithSimpleMessage_ShouldWork() {
            // Arrange
            ResourceNotFoundException exception = new ResourceNotFoundException("Resource not found");

            // Act
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleResourceNotFoundException(exception);

            // Assert
            assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
            assertEquals("Resource not found", response.getBody().get("message"));
        }
    }

    @Nested
    @DisplayName("DuplicateResourceException Handler Tests")
    class DuplicateResourceExceptionTests {

        @Test
        @DisplayName("Should return CONFLICT status with correct response")
        void handleDuplicateResourceException_ShouldReturnConflict() {
            // Arrange
            DuplicateResourceException exception = new DuplicateResourceException("User", "username", "testuser");

            // Act
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleDuplicateResourceException(exception);

            // Assert
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse((Boolean) response.getBody().get("success"));
            assertTrue(response.getBody().get("message").toString().contains("already exists"));
            assertEquals(HttpStatus.CONFLICT.value(), response.getBody().get("status"));
        }

        @Test
        @DisplayName("Should handle simple message constructor")
        void handleDuplicateResourceException_WithSimpleMessage_ShouldWork() {
            // Arrange
            DuplicateResourceException exception = new DuplicateResourceException("Duplicate entry");

            // Act
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleDuplicateResourceException(exception);

            // Assert
            assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
            assertEquals("Duplicate entry", response.getBody().get("message"));
        }
    }

    @Nested
    @DisplayName("InvalidCredentialsException Handler Tests")
    class InvalidCredentialsExceptionTests {

        @Test
        @DisplayName("Should return UNAUTHORIZED status")
        void handleInvalidCredentialsException_ShouldReturnUnauthorized() {
            // Arrange
            InvalidCredentialsException exception = new InvalidCredentialsException("Invalid username or password");

            // Act
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleInvalidCredentialsException(exception);

            // Assert
            assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse((Boolean) response.getBody().get("success"));
            assertEquals("Invalid username or password", response.getBody().get("message"));
            assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getBody().get("status"));
        }
    }

    @Nested
    @DisplayName("UnauthorizedAccessException Handler Tests")
    class UnauthorizedAccessExceptionTests {

        @Test
        @DisplayName("Should return FORBIDDEN status")
        void handleUnauthorizedAccessException_ShouldReturnForbidden() {
            // Arrange
            UnauthorizedAccessException exception = new UnauthorizedAccessException("Access denied");

            // Act
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleUnauthorizedAccessException(exception);

            // Assert
            assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse((Boolean) response.getBody().get("success"));
            assertEquals("Access denied", response.getBody().get("message"));
            assertEquals(HttpStatus.FORBIDDEN.value(), response.getBody().get("status"));
        }
    }

    @Nested
    @DisplayName("MethodArgumentNotValidException Handler Tests")
    class ValidationExceptionTests {

        @Test
        @DisplayName("Should return BAD_REQUEST status with field errors")
        void handleValidationException_ShouldReturnBadRequestWithErrors() {
            // Arrange
            MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            
            FieldError fieldError1 = new FieldError("user", "username", "Username is required");
            FieldError fieldError2 = new FieldError("user", "email", "Invalid email format");
            
            when(exception.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

            // Act
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidationException(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse((Boolean) response.getBody().get("success"));
            assertEquals("Validation failed", response.getBody().get("message"));
            
            @SuppressWarnings("unchecked")
            Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
            assertNotNull(errors);
            assertEquals("Username is required", errors.get("username"));
            assertEquals("Invalid email format", errors.get("email"));
        }

        @Test
        @DisplayName("Should handle empty field errors")
        void handleValidationException_WithEmptyErrors_ShouldWork() {
            // Arrange
            MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
            BindingResult bindingResult = mock(BindingResult.class);
            
            when(exception.getBindingResult()).thenReturn(bindingResult);
            when(bindingResult.getFieldErrors()).thenReturn(List.of());

            // Act
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleValidationException(exception);

            // Assert
            assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
            
            @SuppressWarnings("unchecked")
            Map<String, String> errors = (Map<String, String>) response.getBody().get("errors");
            assertTrue(errors.isEmpty());
        }
    }

    @Nested
    @DisplayName("Generic Exception Handler Tests")
    class GenericExceptionTests {

        @Test
        @DisplayName("Should return INTERNAL_SERVER_ERROR status")
        void handleGenericException_ShouldReturnInternalServerError() {
            // Arrange
            Exception exception = new RuntimeException("Something went wrong");

            // Act
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertNotNull(response.getBody());
            assertFalse((Boolean) response.getBody().get("success"));
            assertTrue(response.getBody().get("message").toString().contains("Something went wrong"));
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().get("status"));
        }

        @Test
        @DisplayName("Should handle null message exception")
        void handleGenericException_WithNullMessage_ShouldWork() {
            // Arrange
            Exception exception = new RuntimeException();

            // Act
            ResponseEntity<Map<String, Object>> response = exceptionHandler.handleGenericException(exception);

            // Assert
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
            assertTrue(response.getBody().get("message").toString().contains("null"));
        }
    }

    @Nested
    @DisplayName("Response Structure Tests")
    class ResponseStructureTests {

        @Test
        @DisplayName("All responses should contain timestamp")
        void allResponses_ShouldContainTimestamp() {
            // Arrange & Act
            ResponseEntity<Map<String, Object>> response1 = exceptionHandler.handleResourceNotFoundException(
                    new ResourceNotFoundException("Not found"));
            ResponseEntity<Map<String, Object>> response2 = exceptionHandler.handleDuplicateResourceException(
                    new DuplicateResourceException("Duplicate"));
            ResponseEntity<Map<String, Object>> response3 = exceptionHandler.handleInvalidCredentialsException(
                    new InvalidCredentialsException("Invalid"));
            ResponseEntity<Map<String, Object>> response4 = exceptionHandler.handleGenericException(
                    new RuntimeException("Error"));

            // Assert
            assertNotNull(response1.getBody().get("timestamp"));
            assertNotNull(response2.getBody().get("timestamp"));
            assertNotNull(response3.getBody().get("timestamp"));
            assertNotNull(response4.getBody().get("timestamp"));
        }

        @Test
        @DisplayName("All responses should have success as false")
        void allResponses_ShouldHaveSuccessFalse() {
            // Arrange & Act
            ResponseEntity<Map<String, Object>> response1 = exceptionHandler.handleResourceNotFoundException(
                    new ResourceNotFoundException("Not found"));
            ResponseEntity<Map<String, Object>> response2 = exceptionHandler.handleDuplicateResourceException(
                    new DuplicateResourceException("Duplicate"));

            // Assert
            assertFalse((Boolean) response1.getBody().get("success"));
            assertFalse((Boolean) response2.getBody().get("success"));
        }
    }
}
