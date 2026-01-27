package com.example.mindStreamApplication.AI.Models.Response;

import java.util.Map;

import lombok.Data;

@Data
public class SearchResponse {
    private Map<String,String> filters;
    private String query;
    private Boolean successful;
    private String mcpValidation;

    
}
