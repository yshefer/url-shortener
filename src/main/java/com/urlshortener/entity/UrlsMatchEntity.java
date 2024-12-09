package com.urlshortener.entity;

import lombok.Getter;

@Getter
public class UrlsMatchEntity {
    private final String shortUrlId;
    private final String longUrl;

    public UrlsMatchEntity(String shortUrlId, String longUrl) {
        this.shortUrlId = shortUrlId;
        this.longUrl = longUrl;
    }
}
