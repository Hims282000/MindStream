package com.example.mindStreamApplication.AI.MCP.Rules;

import org.springframework.stereotype.Component;

@Component
public class QwenRuleEngine {

    public String applyCodeRules(String codeOutput){
        String processed= codeOutput;
        processed= removeUnsafePatterns(processed);
        processed= validateStructure(processed);
        processed = applyFormatting(processed);
        return processed;

    }

    private String removeUnsafePatterns(String code){
        String[] unsafePatterns ={
            "os.system", "subprocess", "exec(", "eval(", "__import__",
            "open(", "write(", "delete", "rm ", "shutil"
        };

        for(String pattern:unsafePatterns){
            if (code.contains(pattern)) {
                code=code.replace(pattern, "# REMOVED" +pattern);
                
            }
        }
        return code;
    }

    private String validateStructure(String code){
        if (!code.contains("def ")&& !code.contains("print(")) {
            return "#Invalid structure detected \n"+code;
            
        }
        return code;
    }

    private String applyFormatting(String code){
        return code.trim();
    }
    
}
