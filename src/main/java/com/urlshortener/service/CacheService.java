package com.urlshortener.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Service for managing a cache of short URL IDs and their corresponding long URLs.
 *
 * <p>The cache size is configurable via application properties using the key {@code app.cache.size}.</p>
 * <p>If the cache size limit is reached, older entries may be evicted to make room for new ones.</p>
 *
 */
@Service
public class CacheService {

    private final Map<String, String> shortLongUrlMap;

    /**
     * Constructs a CacheService with a specified cache size.
     *
     * @param cacheSize the maximum number of entries the cache can hold.
     */
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

    /**
     * Caches the mapping of a short URL ID to its corresponding long URL.
     * If the cache exceeds the defined size, older entries may be evicted.
     *
     * @param shortUrlId the unique identifier for the short URL.
     * @param longUrl    the corresponding long URL.
     */
    public void cache(String shortUrlId, String longUrl) {
        shortLongUrlMap.put(shortUrlId, longUrl);
    }

    /**
     * Retrieves the long URL corresponding to the given short URL ID.
     *
     * @param shortUrlId the unique identifier for the short URL.
     * @return the long URL associated with the short URL ID, or null if not found.
     */
    public String getLongUrl(String shortUrlId) {
        return shortLongUrlMap.get(shortUrlId);
    }
}
