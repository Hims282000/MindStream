package com.example.mindStreamApplication.AI.MCP.Server;

import org.springframework.stereotype.Component;

@Component
public class MCPServer {
    public String processWithMCP(String rawInput, String mcpRules){
        return applyMCPRules(rawInput,mcpRules);

    }

    private String applyMCPRules(String input, String rules){
        return "MCP-processed"+input;
    }
    
}
