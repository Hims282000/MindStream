package com.example.mindStreamApplication.AI.Models.Request;

import lombok.Data;

@Data
public class QwenRequest {
    private String prompt;
    private Integer maxTokens;
    private Double temperature;
    private String model;
    
    
}
