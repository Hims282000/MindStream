package com.example.mindStreamApplication.AI.MCP.Rules;

import org.springframework.stereotype.Component;

@Component
public class CodeOutputRules {

    public boolean isValidPythonCode(String code){
        if (code == null || code.trim().isEmpty()) {
            return false;
            
        }
        boolean hasFunction =code.contains("def ");
        boolean hasPrint= code.contains("print(");
        boolean hasReturn= code.contains("return ");

        return hasFunction || hasPrint || hasReturn;
    }

    public String sanitizedCode(String code){
        return code.replaceAll("(os\\.|subprocess\\.|exec\\(|eval\\()", "# SANITIZED: $1");
    }
    
}
