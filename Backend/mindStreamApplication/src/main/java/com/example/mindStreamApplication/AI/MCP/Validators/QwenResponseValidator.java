package com.example.mindStreamApplication.AI.MCP.Validators;

import org.springframework.stereotype.Component;

@Component
public class QwenResponseValidator {
    public boolean isValidResponse(String response){
        if (response == null || response.trim().isEmpty()) {
            return false;
            
        }

        boolean hasContent= response.length() >10;
        boolean hasPythonIndicators= response.contains("def ") || response.contains("print(");
        boolean hasText= response.matches(".*[a-zA-Z]{3,}.*");
        return hasContent & (hasPythonIndicators || hasText);
    }

    public String getValidationMessage(String message){
        if (message == null) {
            return "Response is null";

            
        }
        if (message.trim().isEmpty()) {
            return "Response is empty";
            
        }
        if (message.length()<10) {
            return "Response too short";
            
        }
        return "Valid";
    }
    
}
