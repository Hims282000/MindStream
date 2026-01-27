package com.example.mindStreamApplication.AI.MCP.Rules;

import org.springframework.stereotype.Component;

@Component
public class SummaryRules {

    public boolean containsSpoilers(String text){
        String[] spoilerKeywords= {
            "dies", "kills", "ending", "reveals", "twist",
            "death", "murder", "betrayal", "secret"
        };

        String lowerText = text.toLowerCase();
        for(String keyword : spoilerKeywords){
            if (lowerText.contains(keyword)) {
                return true;
                
            }
        }
        return false;

    }

    public String enforceMaxLength(String text, int maxLength){
        if (text.length() <= maxLength) {
            return text;
            
        }
        return text.substring(0, maxLength-3) +"....";
    }


    public String adjustTone(String text , String tone ){
        if ("nuetral".equals(tone)) {
            return makeNuetral(text);
            
        }
        else if ("engaging".contains(tone)) {
            return makeEngaging(text);
            
        }

        return text;
    }
    
}
