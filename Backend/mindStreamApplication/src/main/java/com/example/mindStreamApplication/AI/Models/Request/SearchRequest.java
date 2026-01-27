package com.example.mindStreamApplication.AI.Models.Request;

import lombok.Data;

@Data
public class SearchRequest {
    private String query;
    private String language;
    private Boolean useVoice;
    
}
