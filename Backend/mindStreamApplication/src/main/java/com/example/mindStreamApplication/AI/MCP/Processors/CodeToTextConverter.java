package com.example.mindStreamApplication.AI.MCP.Processors;

import org.springframework.stereotype.Component;

@Component
public class CodeToTextConverter {
    public String convert(String code ){
        if (code==null || code.trim().isEmpty()) {
            return "";
            
        }

        String cleaned = code.replaceAll("```python\\n|```\\n|```", "");
        cleaned= cleaned.replaceAll("def\\s+\\w+\\s*\\(.*?\\):", "");
        cleaned= cleaned.replaceAll("#.*", "");
        cleaned= cleaned.replaceAll("(?m)^\\s*$\\n", "");
        return extractQuotedText(cleaned);
    }

    private String extractQuotedText(String text){
        String[] parts= text.split("[\"']");
        if (parts.length >=2) {
            for(int i=1; i<parts.length;i+=2){
                if (parts[i].length()>10) {
                    return parts[i];
                    
                }
            }
            
        }
        return text.trim();
    }
    
}
