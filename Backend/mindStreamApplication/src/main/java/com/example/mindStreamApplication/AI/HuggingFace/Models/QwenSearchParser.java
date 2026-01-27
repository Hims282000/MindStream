package com.example.mindStreamApplication.AI.HuggingFace.Models;

import org.springframework.stereotype.Component;

import com.example.mindStreamApplication.AI.HuggingFace.config.PromptTemplates;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class QwenSearchParser {

    private final QwenCoderModel qwenCoderModel;
    private final PromptTemplates promptTemplates;
    private final ObjectMapper objectMapper;

    public Mono<JsonNode> parseVoiceQuery(String query){
        String prompt= promptTemplates.getSearchPrompt(query);
        return qwenCoderModel.executeCodePrompt(prompt).map(this:: extractJsonFromResponse);
    }

    private JsonNode extractJsonFromResponse (String response) throws Exception{
        int start= response.indexOf('{');
        int end= response.lastIndexOf('}');
        if(start >=0 && end>start){
            String jsonStr= response.substring(start, end+1);
            return objectMapper.readTree(jsonStr);

        }
        throw new RuntimeException("No json found in the response ");

    }

    
}
