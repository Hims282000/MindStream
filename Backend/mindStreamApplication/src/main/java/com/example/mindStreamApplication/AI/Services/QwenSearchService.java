package com.example.mindStreamApplication.AI.Services;

import org.springframework.stereotype.Service;

import com.example.mindStreamApplication.AI.HuggingFace.Models.QwenSearchParser;
import com.example.mindStreamApplication.AI.MCP.Processors.JSONSanitizer;
import com.example.mindStreamApplication.AI.MCP.Rules.SearchRules;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class QwenSearchService {
    private final QwenSearchParser qwenSearchParser;
    private final SearchRules searchRules;
    private final JSONSanitizer jsonSanitizer;

    public Mono<JsonNode> parseVoiceQuery(String query){
        return qwenSearchParser.parseVoiceQuery(query).map(json->{
            if (!searchRules.validateSearchJSON(json)) {
                throw new RuntimeException("Search JSON Validation Failed");
                
            }
            return jsonSanitizer.sanitize(json);
        });
        
    }

    
}
