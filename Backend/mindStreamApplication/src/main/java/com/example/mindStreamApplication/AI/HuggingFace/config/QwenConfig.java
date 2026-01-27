package com.example.mindStreamApplication.AI.HuggingFace.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "qwen")
public class QwenConfig {
    private String modelId = "Qwen/Qwen2.5-Coder-1.5B";
    private int maxNewTokens = 512;
    private double temperature = 0.2;
    private double topP = 0.95;
    private boolean doSample = true;
    private boolean useCache = true;
}
