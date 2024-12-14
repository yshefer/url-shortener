package com.urlshortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class CacheService {

    private final Map<String, String> shortLongUrlMap;

    public CacheService(@Value("${app.cache.size}") int cacheSize) {
        shortLongUrlMap = Collections.synchronizedMap(
                new LinkedHashMap<>() {
                    @Override
                    protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                        return size() > cacheSize;
                    }
                }
        );
    }

    public void cache(String shortUrlId, String longUrl) {
        shortLongUrlMap.put(shortUrlId, longUrl);
    }

    public String getLongUrl(String shortUrlId) {
        return shortLongUrlMap.get(shortUrlId);
    }
}
