package com.urlshortener.dao;

import com.urlshortener.entity.UrlsMatchEntity;

import java.util.Optional;

public interface UrlShortenerDao {
    Optional<UrlsMatchEntity> findById(String shortUrlId);
    Optional<UrlsMatchEntity> findByLongUrl(String longUrl);
    void insert(String shortUrlId, String longUrl);
}
