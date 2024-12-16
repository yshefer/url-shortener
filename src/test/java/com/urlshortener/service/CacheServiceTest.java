package com.urlshortener.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CacheServiceTest {

    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        cacheService = new CacheService(3);
    }

    @Test
    void cache() {
        cacheService.cache("foo", "http://example.com/foo");
        cacheService.cache("bar", "http://example.com/bar");
        cacheService.cache("buz", "http://example.com/buz");
        assertEquals("http://example.com/foo", cacheService.getLongUrl("foo"));
        assertEquals("http://example.com/bar", cacheService.getLongUrl("bar"));
        assertEquals("http://example.com/buz", cacheService.getLongUrl("buz"));
        assertNull(cacheService.getLongUrl("xyz"));

        cacheService.cache("xyz", "http://example.com/xyz");
        assertNull(cacheService.getLongUrl("foo")); // evicted due to cache size
        assertEquals("http://example.com/bar", cacheService.getLongUrl("bar"));
        assertEquals("http://example.com/buz", cacheService.getLongUrl("buz"));
        assertEquals("http://example.com/xyz", cacheService.getLongUrl("xyz"));
    }

}
