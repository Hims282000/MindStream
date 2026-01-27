package com.example.mindStreamApplication.AI.Services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.mindStreamApplication.AI.HuggingFace.Models.QwenSearchParser;
import com.example.mindStreamApplication.AI.MCP.Processors.JSONSanitizer;
import com.example.mindStreamApplication.AI.MCP.Rules.SearchRules;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QwenSearchServiceTest {
    
    @Mock
    private QwenSearchParser qwenSearchParser;
    
    @Mock
    private SearchRules searchRules;
    
    @Mock
    private JSONSanitizer jsonSanitizer;
    
    @InjectMocks
    private QwenSearchService searchService;
    
    @Test
    void testParseVoiceQuery_Success() throws Exception {
        // Given
        String query = "find comedy movies";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rawJson = mapper.readTree("""
            {"genre": "comedy", "mood": "happy", "duration": null, "year": "2023", "type": "movie"}
            """);
        JsonNode sanitizedJson = mapper.readTree("""
            {"genre": "comedy", "mood": "happy", "duration": null, "year": "2023", "type": "movie"}
            """);
        
        when(qwenSearchParser.parseVoiceQuery(query))
            .thenReturn(Mono.just(rawJson));
        when(searchRules.validateSearchJSON(rawJson))
            .thenReturn(true);
        when(jsonSanitizer.sanitize(rawJson))
            .thenReturn(sanitizedJson);
        
        // When & Then
        StepVerifier.create(searchService.parseVoiceQuery(query))
            .expectNext(sanitizedJson)
            .verifyComplete();
        
        verify(searchRules).validateSearchJSON(rawJson);
        verify(jsonSanitizer).sanitize(rawJson);
    }
    
    @Test
    void testParseVoiceQuery_InvalidJSON_ThrowsException() throws Exception {
        // Given
        String query = "invalid query";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode invalidJson = mapper.readTree("""
            {"genre": "invalid-genre", "mood": "invalid-mood"}
            """);
        
        when(qwenSearchParser.parseVoiceQuery(query))
            .thenReturn(Mono.just(invalidJson));
        when(searchRules.validateSearchJSON(invalidJson))
            .thenReturn(false);
        
        // When & Then
        StepVerifier.create(searchService.parseVoiceQuery(query))
            .expectError(RuntimeException.class)
            .verify();
        
        verify(searchRules).validateSearchJSON(invalidJson);
        verify(jsonSanitizer, never()).sanitize(any());
    }
    
    @Test
    void testParseVoiceQuery_ParserError_PropagatesError() {
        // Given
        String query = "error query";
        
        when(qwenSearchParser.parseVoiceQuery(query))
            .thenReturn(Mono.error(new RuntimeException("Parser error")));
        
        // When & Then
        StepVerifier.create(searchService.parseVoiceQuery(query))
            .expectError(RuntimeException.class)
            .verify();
        
        verify(searchRules, never()).validateSearchJSON(any());
        verify(jsonSanitizer, never()).sanitize(any());
    }
    
    @Test
    void testParseVoiceQuery_NullFields_SanitizesProperly() throws Exception {
        // Given
        String query = "query with nulls";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rawJson = mapper.readTree("""
            {"genre": null, "mood": null, "duration": null, "year": null, "type": null}
            """);
        JsonNode sanitizedJson = mapper.readTree("""
            {"genre": null, "mood": null, "duration": null, "year": null, "type": null}
            """);
        
        when(qwenSearchParser.parseVoiceQuery(query))
            .thenReturn(Mono.just(rawJson));
        when(searchRules.validateSearchJSON(rawJson))
            .thenReturn(true);
        when(jsonSanitizer.sanitize(rawJson))
            .thenReturn(sanitizedJson);
        
        // When & Then
        StepVerifier.create(searchService.parseVoiceQuery(query))
            .expectNext(sanitizedJson)
            .verifyComplete();
    }
    
    @Test
    void testParseVoiceQuery_ExtraFields_RemovedBySanitizer() throws Exception {
        // Given
        String query = "query with extra fields";
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rawJson = mapper.readTree("""
            {
                "genre": "action",
                "mood": "exciting",
                "duration": "medium",
                "year": "2023",
                "type": "movie",
                "extraField": "should be removed",
                "anotherExtra": 123
            }
            """);
        JsonNode sanitizedJson = mapper.readTree("""
            {"genre": "action", "mood": "exciting", "duration": "medium", "year": "2023", "type": "movie"}
            """);
        
        when(qwenSearchParser.parseVoiceQuery(query))
            .thenReturn(Mono.just(rawJson));
        when(searchRules.validateSearchJSON(rawJson))
            .thenReturn(true);
        when(jsonSanitizer.sanitize(rawJson))
            .thenReturn(sanitizedJson);
        
        // When & Then
        StepVerifier.create(searchService.parseVoiceQuery(query))
            .expectNext(sanitizedJson)
            .verifyComplete();
        
        // Verify extra fields are removed
        assertFalse(sanitizedJson.has("extraField"));
        assertFalse(sanitizedJson.has("anotherExtra"));
    }
}
