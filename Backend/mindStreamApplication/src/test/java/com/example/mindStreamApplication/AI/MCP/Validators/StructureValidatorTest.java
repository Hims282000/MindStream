package com.example.mindStreamApplication.AI.MCP.Validators;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StructureValidatorTest {
    
    @Autowired
    private StructureValidator validator;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @Test
    void testIsValidSummaryStructure_ValidSummary_ReturnsTrue() {
        // Given
        String validSummary = "This is a valid movie summary. It has multiple sentences.";
        
        // When & Then
        assertTrue(validator.isValidSummaryStructure(validSummary));
    }
    
    @Test
    void testIsValidSummaryStructure_TooLong_ReturnsFalse() {
        // Given
        String longSummary = "A".repeat(300); // Over 200 characters
        
        // When & Then
        assertFalse(validator.isValidSummaryStructure(longSummary));
    }
    
    @Test
    void testIsValidSummaryStructure_TooShort_ReturnsFalse() {
        // Given
        String shortSummary = "Short"; // Less than 20 chars
        
        // When & Then
        assertFalse(validator.isValidSummaryStructure(shortSummary));
    }
    
    @Test
    void testIsValidSummaryStructure_NoPeriod_ReturnsFalse() {
        // Given
        String noPeriod = "This summary has no period even though it's long enough";
        
        // When & Then
        assertFalse(validator.isValidSummaryStructure(noPeriod));
    }
    
    @Test
    void testIsValidSummaryStructure_NullInput_ReturnsFalse() {
        // When & Then
        assertFalse(validator.isValidSummaryStructure(null));
    }
    
    @Test
    void testIsValidSearchStructure_ValidJSON_ReturnsTrue() throws Exception {
        // Given
        String json = """
            {
                "genre": "action",
                "mood": "exciting",
                "duration": "medium",
                "year": "2023",
                "type": "movie"
            }
            """;
        JsonNode node = objectMapper.readTree(json);
        
        // When & Then
        assertTrue(validator.isValidSearchStructure(node));
    }
    
    @Test
    void testIsValidSearchStructure_MissingField_ReturnsFalse() throws Exception {
        // Given - JSON with missing required fields (duration, year, type)
        String json = """
            {
                "genre": "action",
                "mood": "exciting"
            }
            """;
        JsonNode node = objectMapper.readTree(json);
        
        // When & Then
        assertFalse(validator.isValidSearchStructure(node));
    }
    
    @Test
    void testIsValidSearchStructure_NotJSONObject_ReturnsFalse() throws Exception {
        // Given
        String json = "[\"action\", \"exciting\"]"; // JSON array, not object
        JsonNode node = objectMapper.readTree(json);
        
        // When & Then
        assertFalse(validator.isValidSearchStructure(node));
    }
    
    @Test
    void testIsValidSearchStructure_NullInput_ReturnsFalse() {
        // When & Then
        assertFalse(validator.isValidSearchStructure(null));
    }
}
