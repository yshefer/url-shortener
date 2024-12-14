package com.urlshortener.service;

import com.urlshortener.repository.UrlShortenerRepository;
import com.urlshortener.entity.UrlsMatchEntity;
import com.urlshortener.exception.ShortURLGenerationException;
import com.urlshortener.exception.ShortURLNotFoundException;
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

    private final UrlShortenerRepository urlShortenerRepository;
    private final CacheService cacheService;

    @Value("${app.shortUrls.retryNumber}")
    private int retryNumber;

    @Value("${app.shortUrls.idLength}")
    private int shortIdLength;

    public UrlShortenerServiceImpl(@Autowired UrlShortenerRepository urlShortenerRepository,
                                   @Autowired CacheService cacheService) {
        this.urlShortenerRepository = urlShortenerRepository;
        this.cacheService = cacheService;
    }

    @Override
    public String createShortUrl(String longUrl) {
        String fullLongUrl = addHttpsPrefix(longUrl);
        Optional<UrlsMatchEntity> urlsMatchEntity = urlShortenerRepository.findByLongUrl(fullLongUrl);
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
        Optional<UrlsMatchEntity> urlsMatchEntity = urlShortenerRepository.findById(shortUrlId);
        if (urlsMatchEntity.isEmpty()) {
            String message = String.format("Short url with id %s doesn't exist", shortUrlId);
            log.info(message);
            throw new ShortURLNotFoundException(message);
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
                urlShortenerRepository.insert(shortUrlId, fullLongUrl);
                return shortUrlId;
            } catch (DataIntegrityViolationException ex) {
                log.warn("Generated short URL ID {} already exists", shortUrlId);
            }
        }
        log.error("Haven't generated a short URL id for {}. Increase app.shortUrls.idLength in configuration", fullLongUrl);
        throw new ShortURLGenerationException("Haven't generated a short URL. Try again.");
    }
}
