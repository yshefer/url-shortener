package com.urlshortener.controller;

import com.urlshortener.dto.UrlDto;
import com.urlshortener.exception.ShortURLNotFoundException;
import com.urlshortener.service.UrlShortenerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.FOUND;

class UrlShortenerControllerTest {

    @InjectMocks
    private UrlShortenerController urlShortenerController;

    @Mock
    private UrlShortenerService urlShortenerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatesShortUrl() {
        String longUrl = "https://example.com";
        String shortUrl = "abc123";
        String host = "http://localhost:8080/";

        urlShortenerController.setHostUrl(host);

        when(urlShortenerService.createShortUrlId(longUrl)).thenReturn(shortUrl);

        UrlDto response = urlShortenerController.createShortUrl(new UrlDto(longUrl));
        assertNotNull(response);
        assertEquals(host + shortUrl, response.getUrl());
    }

    @Test
    void testGetLongUrl() {
        String shortUrl = "abc123";
        String longUrl = "https://example.com";

        when(urlShortenerService.getLongUrl(shortUrl)).thenReturn(longUrl);

        ResponseEntity<String> response = urlShortenerController.getLongUrl(shortUrl);
        assertNotNull(response);
        assertEquals(FOUND, response.getStatusCode());
        assertEquals(longUrl, response.getHeaders().getLocation().toString());
    }

    @Test
    void testWhenShortURLNotFound() {
        String shortUrl = "abc123";
        when(urlShortenerService.getLongUrl(shortUrl)).thenThrow(new ShortURLNotFoundException("Short URL not found"));
        assertThrows(ShortURLNotFoundException.class, () -> urlShortenerController.getLongUrl(shortUrl));
    }

    @Test
    void testTooLongURL() {
        String url = String.valueOf(new char[3000]);
        UrlDto urlDto = new UrlDto(url);
        assertThrows(IllegalArgumentException.class, () -> urlShortenerController.createShortUrl(urlDto));
    }
}