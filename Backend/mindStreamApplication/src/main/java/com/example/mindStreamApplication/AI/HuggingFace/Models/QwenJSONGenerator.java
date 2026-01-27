package com.example.mindStreamApplication.AI.HuggingFace.Models;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class QwenJSONGenerator{
    private final QwenSearchParser qwenSearchParser;

    public JsonNode generatedStructureJson(String query) throws Exception{
        return qwenSearchParser.parseVoiceQuery(query).block();

    }

}
