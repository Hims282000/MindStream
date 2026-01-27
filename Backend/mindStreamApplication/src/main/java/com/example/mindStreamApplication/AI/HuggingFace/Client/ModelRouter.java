package com.example.mindStreamApplication.AI.HuggingFace.Client;

import org.springframework.stereotype.Component;

@Component
public class ModelRouter {
    private final QwenClient qwenClient;

    public ModelRouter(QwenClient qwenClient) {
        this.qwenClient = qwenClient;
    }

    public QwenClient getQwenClient(){
        return qwenClient;
    }
    
}
