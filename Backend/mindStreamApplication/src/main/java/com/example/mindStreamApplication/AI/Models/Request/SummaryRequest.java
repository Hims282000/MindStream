package com.example.mindStreamApplication.AI.Models.Request;

import lombok.Data;

@Data
public class SummaryRequest {

    private String title;
    private String description;
    private String mediaType;
    private String targetTone;
    

}
