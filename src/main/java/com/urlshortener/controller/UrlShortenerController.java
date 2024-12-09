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

@RestController
public class UrlShortenerController {

    private static final String ERROR_PAGE_URL = "/error";

    @Value(value = "${app.url}")
    public String HOST_URL;

    @Autowired
    UrlShortenerService urlShortenerService;

    @PostMapping(value = "/generate", consumes = "application/json")
    public UrlDto generateShortUrl(@RequestBody UrlDto longUrlDto) {
        String shortUrlId = urlShortenerService.createShortUrl(longUrlDto.getUrl());
        return new UrlDto(HOST_URL + shortUrlId);
    }

    @GetMapping(value = "/{shortUrlId}")
    public ResponseEntity<String> getLongUrl(@PathVariable String shortUrlId) {
        String longUrl = urlShortenerService.getLongUrl(shortUrlId);
        if (longUrl == null) {
            return new ResponseEntity<>("Invalid short URL ID: " + shortUrlId, HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(longUrl));
        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}
