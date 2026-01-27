package com.example.mindStreamApplication.AI.HuggingFace.config;

import org.springframework.stereotype.Component;

@Component
public class PromptTemplates {
    
    public String getSummaryPrompt(String title, String description) {
        return String.format("""
            """, title, description);
    }
    
    public String getSearchPrompt(String query) {
        return String.format("""
            """, query, query);
    }
}