package com.example.mindStreamApplication.AI.MCP.Rules;

import org.springframework.stereotype.Component;

@Component
public class QwenRuleEngine {

    public String applyCodeRules(String codeOutput){
        if (codeOutput == null) {
            return "";
        }
        
        String processed = codeOutput;
        processed = removeUnsafePatterns(processed);
        processed = validateStructure(processed);
        processed = applyFormatting(processed);
        return processed;
    }

    private String removeUnsafePatterns(String code){
        if (code == null) {
            return "";
        }
        
        String[] unsafePatterns ={
            "os.system", "subprocess", "exec(", "eval(", "__import__",
            "open(", "write(", "delete", "rm ", "shutil", "import os",
            "import subprocess", "import sys", "import shutil"
        };

        for(String pattern:unsafePatterns){
            if (code.contains(pattern)) {
                code = code.replace(pattern, "# REMOVED: " + pattern);
            }
        }
        
        // Additional security: remove any shell command execution patterns
        code = code.replaceAll("`.*?`", "# REMOVED: shell command");
        code = code.replaceAll("\\(\\s*\\$.*?\\)", "# REMOVED: shell command");
        
        return code;
    }

    private String validateStructure(String code){
        if (code == null || code.trim().isEmpty()) {
            return "";
        }
        
        // Check if it's valid Python code structure
        boolean hasValidStructure = code.contains("def ") || 
                                   code.contains("print(") || 
                                   code.contains("import ") ||
                                   code.contains("return ") ||
                                   code.contains("=") ||
                                   code.contains(":") ||
                                   code.startsWith("#");
        
        if (!hasValidStructure) {
            return "# Invalid structure detected\n" + code;
        }
        return code;
    }

    private String applyFormatting(String code){
        return code.trim();
    }
    
}
