package com.example.mindStreamApplication.AI.HuggingFace;

import com.example.mindStreamApplication.AI.HuggingFace.Client.HFInferenceClient;
import com.example.mindStreamApplication.AI.HuggingFace.Client.QwenClient;
import com.example.mindStreamApplication.AI.HuggingFace.config.PromptTemplates;
import com.example.mindStreamApplication.AI.HuggingFace.config.QwenConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class QwenClientTest {

    @Mock
    private HFInferenceClient hfInferenceClient;

    @Mock
    private PromptTemplates promptTemplates;

    @Mock
    private QwenConfig qwenConfig;

    @InjectMocks
    private QwenClient qwenClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGenerateCode_Success() throws Exception {
        // Given
        String prompt = "Generate a summary for a movie";
        JsonNode response = objectMapper.readTree("""
            [{"generated_text": "This is a great movie about adventure"}]
            """);
        
        when(qwenConfig.getModelId()).thenReturn("Qwen/Qwen2.5-Coder-32B-Instruct");
        when(qwenConfig.getMaxNewTokens()).thenReturn(512);
        when(qwenConfig.getTemperature()).thenReturn(0.7);
        when(qwenConfig.getTopP()).thenReturn(0.95);
        when(qwenConfig.isDoSample()).thenReturn(true);
        when(hfInferenceClient.callModel(anyString(), anyString(), any()))
            .thenReturn(Mono.just(response));

        // When & Then
        StepVerifier.create(qwenClient.generateCode(prompt))
            .expectNext("This is a great movie about adventure")
            .verifyComplete();
    }

    @Test
    void testGenerateCode_EmptyArray_ReturnsJsonString() throws Exception {
        // Given
        String prompt = "Generate a summary";
        JsonNode response = objectMapper.readTree("[]");
        
        when(qwenConfig.getModelId()).thenReturn("Qwen/Qwen2.5-Coder-32B-Instruct");
        when(qwenConfig.getMaxNewTokens()).thenReturn(512);
        when(qwenConfig.getTemperature()).thenReturn(0.7);
        when(qwenConfig.getTopP()).thenReturn(0.95);
        when(qwenConfig.isDoSample()).thenReturn(true);
        when(hfInferenceClient.callModel(anyString(), anyString(), any()))
            .thenReturn(Mono.just(response));

        // When & Then
        StepVerifier.create(qwenClient.generateCode(prompt))
            .expectNext("[]")
            .verifyComplete();
    }

    @Test
    void testGenerateCode_NoGeneratedText_ReturnsJsonString() throws Exception {
        // Given
        String prompt = "Generate a summary";
        JsonNode response = objectMapper.readTree("""
            [{"other_field": "value"}]
            """);
        
        when(qwenConfig.getModelId()).thenReturn("Qwen/Qwen2.5-Coder-32B-Instruct");
        when(qwenConfig.getMaxNewTokens()).thenReturn(512);
        when(qwenConfig.getTemperature()).thenReturn(0.7);
        when(qwenConfig.getTopP()).thenReturn(0.95);
        when(qwenConfig.isDoSample()).thenReturn(true);
        when(hfInferenceClient.callModel(anyString(), anyString(), any()))
            .thenReturn(Mono.just(response));

        // When & Then
        StepVerifier.create(qwenClient.generateCode(prompt))
            .expectNextMatches(result -> result.contains("other_field"))
            .verifyComplete();
    }

    @Test
    void testGenerateCode_Error_PropagatesError() {
        // Given
        String prompt = "Generate a summary";
        
        when(qwenConfig.getModelId()).thenReturn("Qwen/Qwen2.5-Coder-32B-Instruct");
        when(qwenConfig.getMaxNewTokens()).thenReturn(512);
        when(qwenConfig.getTemperature()).thenReturn(0.7);
        when(qwenConfig.getTopP()).thenReturn(0.95);
        when(qwenConfig.isDoSample()).thenReturn(true);
        when(hfInferenceClient.callModel(anyString(), anyString(), any()))
            .thenReturn(Mono.error(new RuntimeException("API error")));

        // When & Then
        StepVerifier.create(qwenClient.generateCode(prompt))
            .expectError(RuntimeException.class)
            .verify();
    }
}
