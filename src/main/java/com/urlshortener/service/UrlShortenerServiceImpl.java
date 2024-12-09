package com.urlshortener.service;

import com.urlshortener.dao.UrlShortenerDao;
import com.urlshortener.entity.UrlsMatchEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    public static final String HTTPS_PROTOCOL = "https://";
    public static final String HTTP_PROTOCOL = "http://";

    private final UrlShortenerDao urlShortenerDao;
    private final UrlIdGenerationService urlIdGenerationService;

    @Value("${app.retryNumber}")
    private int retryNumber;

    public UrlShortenerServiceImpl(@Autowired UrlShortenerDao urlShortenerDao,
                                   @Autowired UrlIdGenerationService urlIdGenerationService) {
        this.urlShortenerDao = urlShortenerDao;
        this.urlIdGenerationService = urlIdGenerationService;
    }

    @Override
    public String createShortUrl(String longUrl) {
        String fullLongUrl = addHttpPrefix(longUrl);
        Optional<UrlsMatchEntity> urlsMatchEntity = urlShortenerDao.findByLongUrl(fullLongUrl);
        if (urlsMatchEntity.isPresent()) {
            return urlsMatchEntity.get().getShortUrlId();
        }
        String shortUrlId;
        for (int i = 0; i < retryNumber; i++) {
            shortUrlId = urlIdGenerationService.generateUrl();
            urlsMatchEntity = urlShortenerDao.findById(shortUrlId);
            if (urlsMatchEntity.isEmpty()) {
                urlShortenerDao.save(new UrlsMatchEntity(shortUrlId, fullLongUrl));
                return shortUrlId;
            }
        }
        throw new RuntimeException("Haven't generated a short URL. Try again");
    }

    @Override
    public String getLongUrl(String shortUrlId) {
        Optional<UrlsMatchEntity> urlsMatchEntity = urlShortenerDao.findById(shortUrlId);
        if (urlsMatchEntity.isEmpty()) {
            return null;
        }
        return urlsMatchEntity.get().getLongUrl();
    }

    private String addHttpPrefix(String longUrl) {
        if (longUrl.startsWith(HTTPS_PROTOCOL) || longUrl.startsWith(HTTP_PROTOCOL)) {
            return longUrl;
        }
        return HTTP_PROTOCOL + longUrl;
    }
}
