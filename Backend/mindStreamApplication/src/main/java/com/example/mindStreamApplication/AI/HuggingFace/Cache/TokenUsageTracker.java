package com.example.mindStreamApplication.AI.HuggingFace.Cache;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component
public class TokenUsageTracker {
    private final AtomicLong totalTokenUsed= new AtomicLong(0);

    private final AtomicLong totalRequests= new AtomicLong(0);

    public void recordUsage(int tokens){
        totalTokenUsed.addAndGet(tokens);
        totalRequests.incrementAndGet();
    }
    
    public long getTotalTokensUsed(){
        return totalTokenUsed.get();

    }

    public long getTotalRequets(){
        return totalRequests.get();
    }

    public double getAvarageTokensPerRequest(){
        long requests = totalRequests.get();
        return requests > 0 ? (double) totalTokenUsed.get() /requests:0;
    }

    public void reset(){
        totalRequests.set(0);
        totalTokenUsed.set(0);
    }
}
