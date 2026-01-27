package com.example.mindStreamApplication.AI.HuggingFace.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "huggingface")
public class HFConfig {
    private String apiKey;
    private String baseUrl = "https://api-inference.huggingface.co/models";
    private int timeoutSeconds = 30;
    private int maxRetries = 3;


}