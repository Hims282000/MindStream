package com.example.mindStreamApplication.AI.Services;

import com.example.mindStreamApplication.AI.HuggingFace.Models.QwenSummarizer;
import com.example.mindStreamApplication.AI.MCP.Processors.SpoilerDetector;
import com.example.mindStreamApplication.AI.MCP.Rules.SummaryRules;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QwenSummaryServiceTest {

    @Mock
    private QwenSummarizer qwenSummarizer;

    @Mock
    private SummaryRules summaryRules;

    @Mock
    private SpoilerDetector spoilerDetector;

    @InjectMocks
    private QwenSummaryService summaryService;

    @Test
    void testGenerateMCPCompliantSummary_NoSpoilers_Success() {
        // Given
        String title = "Inception";
        String description = "A mind-bending thriller about dreams within dreams";
        String rawSummary = "A skilled thief navigates dream layers.";
        String processedSummary = "A skilled thief navigates dream layers.";

        when(qwenSummarizer.generateSummary(title, description))
            .thenReturn(Mono.just(rawSummary));
        when(spoilerDetector.containsSpoilers(rawSummary))
            .thenReturn(false);
        when(summaryRules.enforceMaxLength(anyString(), anyInt()))
            .thenReturn(processedSummary);
        when(summaryRules.adjustTone(anyString(), anyString()))
            .thenReturn(processedSummary);

        // When & Then
        StepVerifier.create(summaryService.generateMCPComplaintSummary(title, description))
            .expectNext(processedSummary)
            .verifyComplete();

        verify(spoilerDetector).containsSpoilers(rawSummary);
        verify(spoilerDetector, never()).removeSpoilers(anyString());
    }

    @Test
    void testGenerateMCPCompliantSummary_WithSpoilers_RemovesThem() {
        // Given
        String title = "The Sixth Sense";
        String description = "A boy who sees dead people";
        String rawSummary = "The movie reveals a twist ending where the main character dies.";
        String spoilerFreeSummary = "A psychological thriller with a surprising conclusion.";
        String processedSummary = "A psychological thriller with a surprising conclusion.";

        when(qwenSummarizer.generateSummary(title, description))
            .thenReturn(Mono.just(rawSummary));
        when(spoilerDetector.containsSpoilers(rawSummary))
            .thenReturn(true);
        when(spoilerDetector.removeSpoilers(rawSummary))
            .thenReturn(spoilerFreeSummary);
        when(summaryRules.enforceMaxLength(anyString(), anyInt()))
            .thenReturn(processedSummary);
        when(summaryRules.adjustTone(anyString(), anyString()))
            .thenReturn(processedSummary);

        // When & Then
        StepVerifier.create(summaryService.generateMCPComplaintSummary(title, description))
            .expectNext(processedSummary)
            .verifyComplete();

        verify(spoilerDetector).containsSpoilers(rawSummary);
        verify(spoilerDetector).removeSpoilers(rawSummary);
    }

    @Test
    void testGenerateMCPCompliantSummary_Error_PropagatesError() {
        // Given
        String title = "Error Movie";
        String description = "A movie that causes errors";

        when(qwenSummarizer.generateSummary(title, description))
            .thenReturn(Mono.error(new RuntimeException("Summarizer error")));

        // When & Then
        StepVerifier.create(summaryService.generateMCPComplaintSummary(title, description))
            .expectError(RuntimeException.class)
            .verify();
    }

    @Test
    void testGenerateMCPCompliantSummary_LongSummary_TruncatedByRules() {
        // Given
        String title = "Long Movie";
        String description = "A very long description that needs truncation";
        String rawSummary = "A".repeat(200); // Long summary
        String truncatedSummary = "A".repeat(150);
        String tonedSummary = "A".repeat(150);

        when(qwenSummarizer.generateSummary(title, description))
            .thenReturn(Mono.just(rawSummary));
        when(spoilerDetector.containsSpoilers(rawSummary))
            .thenReturn(false);
        when(summaryRules.enforceMaxLength(anyString(), eq(150)))
            .thenReturn(truncatedSummary);
        when(summaryRules.adjustTone(truncatedSummary, "nuetral"))
            .thenReturn(tonedSummary);

        // When & Then
        StepVerifier.create(summaryService.generateMCPComplaintSummary(title, description))
            .expectNext(tonedSummary)
            .verifyComplete();
    }
}
