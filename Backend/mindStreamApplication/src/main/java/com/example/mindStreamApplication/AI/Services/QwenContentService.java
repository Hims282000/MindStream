package com.example.mindStreamApplication.AI.Services;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QwenContentService {
    private final QwenSummaryService qwenSummaryService;
    private final QwenSearchService qwenSearchService;
    
}
