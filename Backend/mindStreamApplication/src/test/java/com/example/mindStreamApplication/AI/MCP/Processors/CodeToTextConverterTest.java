package com.example.mindStreamApplication.AI.MCP.Processors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CodeToTextConverterTest {
    
    @Autowired
    private CodeToTextConverter converter;
    
    @Test
    void testConvert_WithFunctionDefinition_RemovesFunction() {
        // Given
        String code = """
            def generate_summary():
                return "A great movie"
            """;
        
        // When
        String result = converter.convert(code);
        
        // Then
        assertFalse(result.contains("def generate_summary"));
        assertTrue(result.contains("A great movie"));
    }
    
    @Test
    void testConvert_WithComments_RemovesComments() {
        // Given
        String code = """
            # This is a comment
            def test():
                # Another comment
                return "text"  # Inline comment
            """;
        
        // When
        String result = converter.convert(code);
        
        // Then
        assertFalse(result.contains("#"));
        assertTrue(result.contains("text"));
    }
    
    @Test
    void testConvert_WithCodeBlockMarkers_RemovesThem() {
        // Given
        String code = """
            ```python
            def test():
                return "Hello"
            ```
            """;
        
        // When
        String result = converter.convert(code);
        
        // Then
        assertFalse(result.contains("```"));
        assertTrue(result.contains("Hello"));
    }
    
    @Test
    void testConvert_EmptyInput_ReturnsEmpty() {
        // Given
        String code = "";
        
        // When
        String result = converter.convert(code);
        
        // Then
        assertEquals("", result);
    }
    
    @Test
    void testConvert_NullInput_ReturnsEmpty() {
        // When
        String result = converter.convert(null);
        
        // Then
        assertEquals("", result);
    }
    
    @Test
    void testConvert_WithPrintStatement_ExtractsText() {
        // Given
        String code = """
            print("Extract this text")
            """;
        
        // When
        String result = converter.convert(code);
        
        // Then
        assertEquals("Extract this text", result);
    }
    
    @Test
    void testConvert_ComplexCode_ExtractsCorrectText() {
        // Given
        String code = """
            import json
            
            def process():
                data = {"key": "value"}
                text = "The extracted content"
                print(text)
                return text
            """;
        
        // When
        String result = converter.convert(code);
        
        // Then
        assertTrue(result.contains("The extracted content"));
        assertFalse(result.contains("import json"));
        assertFalse(result.contains("def process"));
    }
}