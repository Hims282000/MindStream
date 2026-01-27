package com.example.mindStreamApplication.AI.HuggingFace.Cache;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.hibernate.cache.spi.entry.CacheEntry;
import org.springframework.stereotype.Component;

@Component
public class QwenCacheManager {

    private final ConcurrentHashMap<String, CacheEntry> cache= new ConcurrentHashMap<>();
    private final long TTL= TimeUnit.MINUTES.toMillis(30);
    public void put(String key, Object value){
        cache.put(key, new CacheEntry(value, System.currentTimeMillis()));
    }

    public Object get(String key){
        CacheEntry entry= cache.get(key);
        if (entry !=null && !isExpired(entry)) {
            return entry.value;
            
        }
        cache.remove(key);
        return null;

    }

    public void remove(String key){
        cache.remove(key);
    }
    
    public void clear(){
        cache.clear();
    }

    private boolean isExpired(CacheEntry entry){
        return System.currentTimeMillis() - entry.timestamp >TTL;

    }

    private static class  CacheEntry{
        Object value;
        long timestamp;

        CacheEntry(Object value,long timestamp){
            this.timestamp=timestamp;
            this.value=value;
        }
    } 
}
