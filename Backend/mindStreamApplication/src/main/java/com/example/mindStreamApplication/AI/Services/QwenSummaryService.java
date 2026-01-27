package com.example.mindStreamApplication.AI.Services;

import org.springframework.stereotype.Service;

import com.example.mindStreamApplication.AI.HuggingFace.Models.QwenSummarizer;
import com.example.mindStreamApplication.AI.MCP.Processors.SpoilerDetector;
import com.example.mindStreamApplication.AI.MCP.Rules.SummaryRules;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QwenSummaryService {

    private final QwenSummarizer qwenSummarizer;
    private final SummaryRules summaryRules;
    private final SpoilerDetector spoilerDetector;

    public Mono<String>generateMCPComplaintSummary(String title, String description){
        return qwenSummarizer.generateSummary(title, description).map(rawSummary -> {
            if (spoilerDetector.containsSpoilers(rawSummary)) {
                rawSummary=spoilerDetector.removeSpoilers(rawSummary);
                
            }
            rawSummary=summaryRules.enforceMaxLength(description, 150);
            rawSummary=summaryRules.adjustTone(rawSummary, "nuetral");

            return rawSummary;
        });

    }
    
}
