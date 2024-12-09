package com.urlshortener.dto;

import lombok.Getter;

@Getter
public class UrlDto {

    private final String url;

    public UrlDto(String url) {
        this.url = url;
    }
}
