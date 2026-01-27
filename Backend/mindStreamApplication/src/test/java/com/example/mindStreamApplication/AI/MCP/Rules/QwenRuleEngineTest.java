package com.example.mindStreamApplication.AI.MCP.Rules;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class QwenRuleEngineTest {
    
    @InjectMocks
    private QwenRuleEngine qwenRuleEngine;
    
    @Test
    void testApplyCodeRules_WithSafeCode_ReturnsProcessedCode() {
        // Given
        String safeCode = """
            def generate_summary():
                title = "Inception"
                description = "A thief who steals secrets"
                summary = "A mind-bending thriller about dreams"
                return summary
            """;
        
        // When
        String result = qwenRuleEngine.applyCodeRules(safeCode);
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("def generate_summary"));
        assertFalse(result.contains("# REMOVED"));
    }
    
    @Test
    void testApplyCodeRules_WithUnsafePatterns_RemovesThem() {
        // Given
        String unsafeCode = """
            import os
            def dangerous():
                os.system("rm -rf /")
                return "dangerous"
            """;
        
        // When
        String result = qwenRuleEngine.applyCodeRules(unsafeCode);
        
        // Then
        assertNotNull(result);
        assertTrue(result.contains("# REMOVED: os.system"));
        assertFalse(result.contains("rm -rf /"));
    }
    
    @Test
    void testApplyCodeRules_WithInvalidStructure_AddsComment() {
        // Given
        String invalidCode = "just random text without structure";
        
        // When
        String result = qwenRuleEngine.applyCodeRules(invalidCode);
        
        // Then
        assertNotNull(result);
        assertTrue(result.startsWith("# Invalid structure detected"));
    }
    
    @Test
    void testApplyCodeRules_WithPrintStatement_PreservesIt() {
        // Given
        String codeWithPrint = """
            def test():
                print("Hello World")
                return "done"
            """;
        
        // When
        String result = qwenRuleEngine.applyCodeRules(codeWithPrint);
        
        // Then
        assertTrue(result.contains("print("));
        assertFalse(result.contains("# REMOVED"));
    }
    
    @Test
    void testApplyCodeRules_EmptyInput_ReturnsEmpty() {
        // Given
        String emptyCode = "";
        
        // When
        String result = qwenRuleEngine.applyCodeRules(emptyCode);
        
        // Then
        assertEquals("", result);
    }
    
    @Test
    void testApplyCodeRules_NullInput_ReturnsEmpty() {
        // Given
        String nullCode = null;
        
        // When
        String result = qwenRuleEngine.applyCodeRules(nullCode);
        
        // Then
        assertEquals("", result);
    }
}
