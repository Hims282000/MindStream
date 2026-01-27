package com.example.mindStreamApplication.AI.MCP.Validators;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

@Component
public class StructureValidator {
    public boolean isValidSummaryStructure(String text){
        if (text== null) {
            return  false;
            
        }
        if (text.length()>200) {
            return false;

            
        }
        boolean hasPeriod= text.contains(".");
        boolean hasContent= text.length() >20;
        return hasContent && hasPeriod;
    }

    public boolean hasValidStructure(JsonNode jsonNode){
        if (jsonNode==null || !jsonNode.isObject()) {
            return false;

            
        }
        String[] required = {"genre", "mood", "duration", "year", "type"};
        for (String field : required) {
            if (!jsonNode.has(field)) return false;
        }
        return true;
    }
    
}
