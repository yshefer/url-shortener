package com.urlshortener.service;

public interface UrlShortenerService {
    String createShortUrl(String longUrl);
    String getLongUrl(String shortUrlId);
}
