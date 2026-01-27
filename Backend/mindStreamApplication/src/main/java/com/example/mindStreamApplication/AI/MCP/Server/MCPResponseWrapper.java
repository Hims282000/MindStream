package com.example.mindStreamApplication.AI.MCP.Server;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class MCPResponseWrapper {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode wrapWithMCPMetadata(JsonNode data, String operation){
        ObjectNode wrapper = objectMapper.createObjectNode();
        wrapper.set("data", data);
        wrapper.put("mcp_version", "1.0");
        wrapper.put("operation", operation);
        wrapper.put("timestamp", System.currentTimeMillis());
        wrapper.put("compliance", "verified");
        return wrapper;
    }
    
}
