package com.example.mindStreamApplication.AI.Models.Response;

import lombok.Data;

@Data
public class SummaryResponse {
    private String summary;
    private Integer length;
    private String tone;
    private Boolean spoilerFree;
    private String mcpCompliance;
    
}
