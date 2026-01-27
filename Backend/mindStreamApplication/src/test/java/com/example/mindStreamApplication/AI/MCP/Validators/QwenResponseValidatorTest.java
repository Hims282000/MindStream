package com.example.mindStreamApplication.AI.MCP.Validators;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class QwenResponseValidatorTest {
    
    @Autowired
    private QwenResponseValidator validator;
    
    @Test
    void testIsValidResponse_ValidPythonCode_ReturnsTrue() {
        // Given
        String validCode = """
            def generate():
                return "A great movie"
            """;
        
        // When & Then
        assertTrue(validator.isValidResponse(validCode));
    }
    
    @Test
    void testIsValidResponse_ValidText_ReturnsTrue() {
        // Given
        String validText = "This is a valid text response with enough content";
        
        // When & Then
        assertTrue(validator.isValidResponse(validText));
    }
    
    @Test
    void testIsValidResponse_TooShort_ReturnsFalse() {
        // Given
        String shortText = "Hi";
        
        // When & Then
        assertFalse(validator.isValidResponse(shortText));
    }
    
    @Test
    void testIsValidResponse_NullInput_ReturnsFalse() {
        // When & Then
        assertFalse(validator.isValidResponse(null));
    }
    
    @Test
    void testIsValidResponse_EmptyString_ReturnsFalse() {
        // Given
        String empty = "";
        
        // When & Then
        assertFalse(validator.isValidResponse(empty));
    }
    
    @Test
    void testIsValidResponse_WhitespaceOnly_ReturnsFalse() {
        // Given
        String whitespace = "   \n  \t  ";
        
        // When & Then
        assertFalse(validator.isValidResponse(whitespace));
    }
    
    @Test
    void testGetValidationMessage_ValidResponse_ReturnsValid() {
        // Given
        String valid = "def test(): return 'valid'";
        
        // When
        String message = validator.getValidationMessage(valid);
        
        // Then
        assertEquals("Valid", message);
    }
    
    @Test
    void testGetValidationMessage_NullResponse_ReturnsErrorMessage() {
        // When
        String message = validator.getValidationMessage(null);
        
        // Then
        assertEquals("Response is null", message);
    }
    
    @Test
    void testGetValidationMessage_ShortResponse_ReturnsErrorMessage() {
        // Given
        String shortResponse = "short";
        
        // When
        String message = validator.getValidationMessage(shortResponse);
        
        // Then
        assertEquals("Response too short", message);
    }
    
    @Test
    void testGetValidationMessage_EmptyResponse_ReturnsErrorMessage() {
        // Given
        String empty = "";
        
        // When
        String message = validator.getValidationMessage(empty);
        
        // Then
        assertEquals("Response is empty", message);
    }
}