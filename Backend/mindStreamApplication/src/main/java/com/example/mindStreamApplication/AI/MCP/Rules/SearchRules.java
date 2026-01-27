package com.example.mindStreamApplication.AI.MCP.Rules;



import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;



@Component
public class SearchRules {

    public boolean validateSearchJSON(JsonNode json){
        if (json == null ) {
            return false;
            
        }

        String[] requiredFields= {"genre","mood","duration","year","type"};
        for(String field:requiredFields){
            if (!json.has(field)) {
                return false;
                
            }
        }

        return isValidGenre(json.get("genre").asText()) && 
                isValidMood(json.get("mood").asText()) &&
                 isValidDuration(json.get("duration").asText()) && 
                 isValidType(json.get("type").asText());

    }

    private boolean isValidGenre(String genre){
        if (genre == null || "null".equals(genre)) {
            return true;
            
        }
        String[] validGenres= {"action","comedy","drama","horror","sci-fi"};
        for(String valid:validGenres){
            if (valid.equals(genre)) {
                return true;
                
            }
            
        }
        return false;
    }


    private boolean isValidMood(String mood){
        if (mood== null || "null".equals(mood)) {
            return true;
            
        }

        String[] validMoods={"happy","sad","exciting","relaxing","thoughtful"};
        for(String valid:validMoods){
            if (valid.equals(mood)) {
                return true;
                
            }
        }
        return false;
    }

    private boolean isValidDuration(String duration){
        if (duration ==null || "null".equals(duration)) {
            return true;
            
        }

        String[] validType= {"short","medium","long"};
        for(String valid:validType){
            if (valid.equals(duration)) {
                return true;
                
            }
        }
        return false;
    }

    private boolean isValidType (String type){
        if (type==null || "null".equals(type)) {
            return true;
        }

        String[] validTypes={"movie","tv_show","album","podcast"};
        for(String valid:validTypes){
            if (valid.equals(type)) {
                return true;
                
            }
        }
        return false;
    }


    
}
