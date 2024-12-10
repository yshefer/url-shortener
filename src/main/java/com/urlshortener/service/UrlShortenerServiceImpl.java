package com.urlshortener.service;

import com.urlshortener.dao.UrlShortenerDao;
import com.urlshortener.entity.UrlsMatchEntity;
import com.urlshortener.utils.RandomStringGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class UrlShortenerServiceImpl implements UrlShortenerService {

    public static final String HTTPS_PROTOCOL = "https://";
    public static final String HTTP_PROTOCOL = "http://";

    private final UrlShortenerDao urlShortenerDao;
    private final UrlIdGenerationService urlIdGenerationService;

    @Value("${app.shortUrls.retryNumber}")
    private int retryNumber;

    @Value("${app.shortUrls.idLength}")
    private int shortIdLength;

    public UrlShortenerServiceImpl(@Autowired UrlShortenerDao urlShortenerDao) {
        this.urlShortenerDao = urlShortenerDao;
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
            shortUrlId = RandomStringGenerator.generateString(shortIdLength);
            try {
                urlShortenerDao.insert(shortUrlId, fullLongUrl);
                return shortUrlId;
            } catch (DataIntegrityViolationException ex) {
                log.warn("Generated short URL ID already exists", ex);
            }
        }
        log.error("Haven't generated a short URL id. Increase app.retryNumber in configuration");
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
        return HTTPS_PROTOCOL + longUrl;
    }
}
