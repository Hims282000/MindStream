package com.example.mindStreamApplication.AI.HuggingFace.Models;

import org.springframework.stereotype.Component;

import com.example.mindStreamApplication.AI.HuggingFace.config.PromptTemplates;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class QwenSummarizer {

    private final QwenCoderModel qwenCoderModel;
    private final PromptTemplates promptTemplates;

    public Mono<String> generateSummary(String title, String description){
        String prompt= promptTemplates.getSummaryPrompt(title, description);
        return qwenCoderModel.executeCodePrompt(prompt);

    }
    
}
