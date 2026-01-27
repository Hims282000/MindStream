package com.example.mindStreamApplication.AI.MCP.Server;

import org.springframework.stereotype.Component;

@Component
public class QwenMCPAdapter {

    public String adaptForQwen(String input){
        return "MCP- Adapted input\n "+input;
    }

    public String extratFromQwen(String qwenOutput){
        return qwenOutput.replaceAll("```python\\n|```\\n|```", "").trim();
    }
    
}
