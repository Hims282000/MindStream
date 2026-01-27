package com.example.mindStreamApplication.AI.HuggingFace.Client;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.mindStreamApplication.AI.HuggingFace.config.HFConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class HFInferenceClient {

    private final HFConfig hfConfig;
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public Mono<JsonNode> callModel(String modelId, String inputs, Map<String, Object> parameters) {
        String url = hfConfig.getBaseUrl() + "/" + modelId;

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", inputs);
        requestBody.put("parameters", parameters);

        return webClient.post()
                .uri(url)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + hfConfig.getApiKey())
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .doOnSuccess(response -> log.debug("HF API call successful"))
                .doOnError(error -> log.error("HF API error: {}", error.getMessage()));
    }
}
