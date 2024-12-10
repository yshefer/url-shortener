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
    private final CacheService cacheService;

    @Value("${app.shortUrls.retryNumber}")
    private int retryNumber;

    @Value("${app.shortUrls.idLength}")
    private int shortIdLength;

    public UrlShortenerServiceImpl(@Autowired UrlShortenerDao urlShortenerDao,
                                   @Autowired CacheService cacheService) {
        this.urlShortenerDao = urlShortenerDao;
        this.cacheService = cacheService;
    }

    @Override
    public String createShortUrl(String longUrl) {
        String fullLongUrl = addHttpsPrefix(longUrl);
        Optional<UrlsMatchEntity> urlsMatchEntity = urlShortenerDao.findByLongUrl(fullLongUrl);
        if (urlsMatchEntity.isPresent()) {
            return urlsMatchEntity.get().getShortUrlId();
        }
        return findFreeShortUrlId(fullLongUrl);
    }

    @Override
    public String getLongUrl(String shortUrlId) {
        String cachedLongUrl = cacheService.getLongUrl(shortUrlId);
        if (cachedLongUrl != null) {
            return cachedLongUrl;
        }
        Optional<UrlsMatchEntity> urlsMatchEntity = urlShortenerDao.findById(shortUrlId);
        if (urlsMatchEntity.isEmpty()) {
            throw new RuntimeException(String.format("Short url with id %s doesn't exist", shortUrlId));
        }
        String longUrl = urlsMatchEntity.get().getLongUrl();
        cacheService.cache(shortUrlId, longUrl);
        return longUrl;
    }

    private String addHttpsPrefix(String longUrl) {
        if (longUrl.startsWith(HTTPS_PROTOCOL) || longUrl.startsWith(HTTP_PROTOCOL)) {
            return longUrl;
        }
        return HTTPS_PROTOCOL + longUrl;
    }

    private String findFreeShortUrlId(String fullLongUrl) {
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
}
