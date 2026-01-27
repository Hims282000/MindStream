package com.example.mindStreamApplication.AI.HuggingFace.Models;

import org.springframework.stereotype.Component;

import com.example.mindStreamApplication.AI.HuggingFace.Client.QwenClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class QwenCoderModel {

    private final QwenClient qwenClient;

    public Mono<String> executeCodePrompt(String prompt){
        return qwenClient.generateCode(prompt);
    }

    
}
