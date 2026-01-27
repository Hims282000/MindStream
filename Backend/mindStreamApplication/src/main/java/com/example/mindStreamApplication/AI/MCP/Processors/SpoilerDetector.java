package com.example.mindStreamApplication.AI.MCP.Processors;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class SpoilerDetector {

    private final List<String> SPOILER_KEYWORDS=Arrays.asList( "dies", "kills", "death", "murder", "ending",
        "reveals", "twist", "betrayal", "secret",
        "plot twist", "finale", "conclusion", "ending scene"
    );

    public boolean containsSpoilers(String text){
        if (text == null) {
            return false;
        }

        String lowerText= text.toLowerCase();
        for(String keyword:SPOILER_KEYWORDS){
            if (lowerText.contains(keyword)) {
                return true; 
            }
        }
        return false;
    }


    public String removeSpoilers(String text){
        if (text == null) {
            return "";
        }

        String result= text;
        for(String keyword:SPOILER_KEYWORDS){
            result= result.replaceAll("(?i)" +keyword, "[spoiler removed]");
        }
        return result;
    }

    
}
