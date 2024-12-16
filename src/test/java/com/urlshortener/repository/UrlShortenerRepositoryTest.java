package com.urlshortener.repository;

import com.urlshortener.entity.UrlsMatchEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UrlShortenerRepositoryTest {

    @Autowired
    private UrlShortenerRepository urlShortenerRepository;

    @Test
    void testInsert() {
        String longUrl = "https://example.com";
        String shortUrl = "abc123";

        urlShortenerRepository.insert(shortUrl, longUrl);

        Optional<UrlsMatchEntity> result = urlShortenerRepository.findById(shortUrl);
        assertTrue(result.isPresent());
        assertEquals(longUrl, result.get().getLongUrl());
        assertEquals(shortUrl, result.get().getShortUrlId());
    }
}
