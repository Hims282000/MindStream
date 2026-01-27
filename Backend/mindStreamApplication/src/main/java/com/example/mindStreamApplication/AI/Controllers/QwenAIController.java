package com.example.mindStreamApplication.AI.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.mindStreamApplication.AI.Controllers.QwenAIController.SummaryResponse;
import com.example.mindStreamApplication.AI.Services.QwenSearchService;
import com.example.mindStreamApplication.AI.Services.QwenSummaryService;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ai/qwen")
public class QwenAIController {

    private final QwenSummaryService qwenSummaryService;
    private final QwenSearchService qwenSearchService;

     @PostMapping("/summary")
    public Mono<ResponseEntity<?>> generateSummary(
            @RequestBody SummaryRequest request) {
        return qwenSummaryService.generateMCPCompliantSummary(
                request.getTitle(), 
                request.getDescription())
            .map(summary -> ResponseEntity.ok(new SummaryResponse(summary)))
            .onErrorResume(e -> Mono.just(
                ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()))));
    }
    
    @PostMapping("/voice-search")
    public Mono<ResponseEntity<?>> voiceSearch(
            @RequestBody VoiceSearchRequest request) {
        return qwenSearchService.parseVoiceQuery(request.getQuery())
            .map(json -> ResponseEntity.ok(json))
            .onErrorResume(e -> Mono.just(
                ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage())));
    }

    public static class SummaryRequest{
        private String title;
        private String description;

        

        public SummaryRequest(String description) {
            this.description = description;
        }
        public String getTitle() {
            return title;
        }
        public void setTitle(String title) {
            this.title = title;
        }
        public String getDescription() {
            return description;
        }
        public void setDescription(String description) {
            this.description = description;
        }

        
        
    }


    public static class  VoiceSearchRequest{
        private String query;

        

        

        public String getQuery() {
            return query;
        }

        public void setQuery(String query) {
            this.query = query;
        }

        
    
        
    }

    public static class SummaryResponse {
        private final String summary;

        public SummaryResponse(String summary) {
            this.summary = summary;
        }

        public String getSummary() {
            return summary;
        }

        

        
    }

    public static class ErrorResponse{
        private final String error;

        public ErrorResponse(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        
    }


}
