package com.example.mindStreamApplication.AI.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/api/v1")
public class AIController {

    @GetMapping("/health")
    public String health(){
        return "AI Service is healthy ";
    }

    @GetMapping("/mcp/version")
    public String mcpVersion(){
        return "MCP Server v1.0 Qwen Integreation";

    }
    
}
