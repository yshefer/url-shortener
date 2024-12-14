package com.urlshortener.controller;

import com.urlshortener.dto.UrlDto;
import com.urlshortener.service.UrlShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class UrlShortenerController {

    private static final int MAX_URL_LENGTH = 2048;
    private static final String URL_IS_TOO_LONG_MSG = "URL is too long";

    @Value(value = "${app.url}")
    public String HOST_URL;

    @Autowired
    UrlShortenerService urlShortenerService;

    @PostMapping(value = "/", consumes = APPLICATION_JSON_VALUE)
    public UrlDto createShortUrl(@RequestBody UrlDto longUrlDto) {
        if (longUrlDto.getUrl().length() > MAX_URL_LENGTH) {
            throw new IllegalArgumentException(URL_IS_TOO_LONG_MSG);
        }
        String shortUrlId = urlShortenerService.createShortUrl(longUrlDto.getUrl());
        return new UrlDto(HOST_URL + shortUrlId);
    }

    @GetMapping(value = "/{shortUrlId}")
    public ResponseEntity<String> getLongUrl(@PathVariable String shortUrlId) {
        String longUrl = urlShortenerService.getLongUrl(shortUrlId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(longUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
