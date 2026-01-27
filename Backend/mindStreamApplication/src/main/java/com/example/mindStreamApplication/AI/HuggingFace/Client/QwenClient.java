package com.example.mindStreamApplication.AI.HuggingFace.Client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.mindStreamApplication.AI.HuggingFace.config.PromptTemplates;
import com.example.mindStreamApplication.AI.HuggingFace.config.QwenConfig;
import com.fasterxml.jackson.databind.JsonNode;

@Slf4j
@Service
@RequiredArgsConstructor
public class QwenClient {

    private final HFInferenceClient hfInferenceClient;
    private final PromptTemplates promptTemplates;
    private final QwenConfig qwenConfig;

    public Mono<String> generateCode(String prompt){
        Map<String,Object> parameters= new HashMap<>();
        parameters.put("max_new_tokens", qwenConfig.getMaxNewTokens());
        parameters.put("temperature", qwenConfig.getTemperature());
        parameters.put("top_p", qwenConfig.getTopP());
        parameters.put("do_Sample", qwenConfig.isDoSample());

        return hfInferenceClient.callModel(qwenConfig.getModelId(), prompt, parameters).map(this::extractGeneratedText);
    }

    private String extractGeneratedText(JsonNode response){
        if (response.isArray() && response.size()> 0) {
            JsonNode first= response.get(0);
            if (first.has("generated_text")) {
                return first.get("generated_text").asText();
                
            }
            
        }
        return response.toString();
    }




}
