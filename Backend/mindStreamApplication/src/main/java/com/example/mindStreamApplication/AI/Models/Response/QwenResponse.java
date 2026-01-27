package com.example.mindStreamApplication.AI.Models.Response;

import lombok.Data;

@Data
public class QwenResponse {
    private String generateCode;
    private String extractedText;
    private String mcpStatus;
    private Long processingTime;

}
