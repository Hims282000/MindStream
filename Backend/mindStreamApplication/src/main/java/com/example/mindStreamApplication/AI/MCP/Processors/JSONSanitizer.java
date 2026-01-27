package com.example.mindStreamApplication.AI.MCP.Processors;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@Component
public class JSONSanitizer {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode sanitize(JsonNode jsonNode){
        if (jsonNode ==null || !jsonNode.isObject()) {
            return createEmptyJSON();
        }

        ObjectNode sanitized= objectMapper.createObjectNode();

        String[] requiredFields={"genre","mood","duration","year","type"};
        for(String field:requiredFields){
            if (jsonNode.has(field) && ! jsonNode.get(field).isNull()) {
                sanitized.set(field, jsonNode.get(field));
            }
            else{
                sanitized.putNull(field);
            }
        }
        return sanitized;
        

    }

    private JsonNode createEmptyJSON(){
        ObjectNode empty= objectMapper.createObjectNode();
        empty.putNull("genre");
        empty.putNull("mood");
        empty.putNull("duration");
        empty.putNull("year");
        empty.putNull("type");
        return empty;
    }

    
    
}
