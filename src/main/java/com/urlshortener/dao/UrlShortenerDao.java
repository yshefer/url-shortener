package com.urlshortener.dao;

import com.urlshortener.entity.UrlsMatchEntity;

import java.util.Optional;

public interface UrlShortenerDao {
    UrlsMatchEntity save(UrlsMatchEntity urlsMatchEntity);
    Optional<UrlsMatchEntity> findById(String shortUrlId);
    Optional<UrlsMatchEntity> findByLongUrl(String longUrl);
}
