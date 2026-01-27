package com.example.mindStreamApplication.AI.MCP.Validators;

import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class CodeSafetyValidator {
    private final List<String> UNSAFE_PATTERNS= Arrays.asList(  "os.system", "subprocess", "exec(", "eval(",
        "__import__", "open(", "write(", "delete",
        "rm ", "shutil", "import os", "import sys");

        public boolean isSafe(String code){
            if (code== null) {
                return false;
                
            }
            String lowerCode= code.toLowerCase();
            for(String pattern: UNSAFE_PATTERNS){
                if (lowerCode.contains(pattern)) {
                    return false;
                }
            }
            return true;
        }

        public String sanitizeUnsafeCode(String code){
            if (code== null) {
                return "";  
            }
            String sanitized= code;
            for(String pattern:UNSAFE_PATTERNS){
                sanitized=sanitized.replaceAll("(?i)"+ pattern, "# REMOVED: "+pattern);
            }
            return sanitized;
        }
    
}
