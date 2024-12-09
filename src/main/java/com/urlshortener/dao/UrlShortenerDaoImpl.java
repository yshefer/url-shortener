package com.urlshortener.dao;

import com.urlshortener.entity.UrlsMatchEntity;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class UrlShortenerDaoImpl implements UrlShortenerDao {

    private final Map<String, String> urlMap = new HashMap<>();

    @Override
    public UrlsMatchEntity save(UrlsMatchEntity urlsMatchEntity) {
        urlMap.put(urlsMatchEntity.getShortUrlId(), urlsMatchEntity.getLongUrl());
        return urlsMatchEntity;
    }

    @Override
    public Optional<UrlsMatchEntity> findById(String shortUrlId) {
        String longUrl = urlMap.get(shortUrlId);
        if (longUrl == null) {
            return Optional.empty();
        }
        return Optional.of(new UrlsMatchEntity(shortUrlId, longUrl));
    }

    @Override
    public Optional<UrlsMatchEntity> findByLongUrl(String longUrl) {
        for (Map.Entry<String, String> entry : urlMap.entrySet()) {
            if (entry.getValue().equals(longUrl)) {
                return Optional.of(new UrlsMatchEntity(entry.getKey(), entry.getValue()));
            }
        }
        return Optional.empty();
    }
}
