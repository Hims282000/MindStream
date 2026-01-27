package com.example.mindStreamApplication.AI.MCP.Processors;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class QwenOutputParser {
    public String extractTextFromCode(String codeOutput){
        if (codeOutput ==null) {
            return "";
            
        }

        Pattern printPattern= Pattern.compile("print\\\\([\\\"']([^\\\"']+)[\\\"']\\\\)");
        Matcher printMatcher=printPattern.matcher(codeOutput);
        if (printMatcher.find()) {
            return printMatcher.group(1);
            
        }

        Pattern returnPattern= Pattern.compile("return [\\\"']([^\\\"']+)[\\\"']");
        Matcher patternMatcher=returnPattern.matcher(codeOutput);
        if (patternMatcher.find()) {
            return patternMatcher.group(1);
            
        }

        Pattern varPattern= Pattern.compile("summary = [\\\"']([^\\\"']+)[\\\"']");
        Matcher matchMatcher= varPattern.matcher(codeOutput);
        if (matchMatcher.find()) {
            return matchMatcher.group(1);
            
        }

        String[] lines = codeOutput.split("\n");
        for(String line:lines){
            line=line.trim();
            if (!line.startsWith("#") && !line.isEmpty() && line.length() >10 ) {
                return line.replaceAll("[\"'']", "").trim();
                
            }
        }
        return codeOutput;

    }


    public String extractJSONFromCode(String codeOutput){
        int start= codeOutput.indexOf('{');
        int end= codeOutput.lastIndexOf('}');
        if (start>=0 && end>start) {
            return codeOutput.substring(start, end-1);
            
        }
        return "{}";
    }
    
}
