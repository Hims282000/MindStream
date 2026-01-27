package com.example.mindStreamApplication.AI.MCP.Validators;


import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeSafetyValidatorTest {
    
    @Autowired
    private CodeSafetyValidator validator;
    
    @Test
    void testIsSafe_SafeCode_ReturnsTrue() {
        String safeCode = """
            def generate_summary():
                return "Safe text"
            """;
        assertTrue(validator.isSafe(safeCode));
    }
    
    @Test
    void testIsSafe_UnsafeCode_ReturnsFalse() {
        String unsafeCode = """
            import os
            os.system("rm -rf /")
            """;
        assertFalse(validator.isSafe(unsafeCode));
    }
    
    @Test
    void testSanitizeUnsafeCode() {
        String unsafeCode = "os.system('command')";
        String sanitized = validator.sanitizeUnsafeCode(unsafeCode);
        assertTrue(sanitized.contains("# REMOVED"));
        assertFalse(sanitized.contains("os.system"));
    }
}
