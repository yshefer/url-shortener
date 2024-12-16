package com.urlshortener.service;

import com.urlshortener.repository.UrlShortenerRepository;
import com.urlshortener.entity.UrlsMatchEntity;
import com.urlshortener.exception.ShortURLGenerationException;
import com.urlshortener.exception.ShortURLNotFoundException;
import com.urlshortener.utils.RandomStringGenerator;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Implementation of the UrlShortenerService interface.
 *
 * <p>This class utilizes the {@link UrlShortenerRepository} for persisting and retrieving short URL IDs and their
 * corresponding long URLs from a database.
 * Additionally, it uses caching for storing URL mappings.
 * </p>
 *
 */
@Service
@Slf4j
public class UrlShortenerServiceImpl implements UrlShortenerService {

    public static final String HTTPS_PROTOCOL = "https://";
    public static final String HTTP_PROTOCOL = "http://";

    private final UrlShortenerRepository urlShortenerRepository;
    private final CacheService cacheService;

    @Value("${app.shortUrls.retryNumber}")
    @Setter
    private int retryNumber;

    @Value("${app.shortUrls.idLength}")
    @Setter
    private int shortIdLength;

    /**
     * Constructs a UrlShortenerServiceImpl with the provided UrlShortenerRepository and CacheService.
     *
     * @param urlShortenerRepository the repository for managing URL mappings in the database.
     * @param cacheService            the service responsible for caching URL mappings.
     */
    public UrlShortenerServiceImpl(@Autowired UrlShortenerRepository urlShortenerRepository,
                                   @Autowired CacheService cacheService) {
        this.urlShortenerRepository = urlShortenerRepository;
        this.cacheService = cacheService;
    }

    /**
     * Creates a short URL ID for the given long URL.
     *
     * <p>If a mapping for the provided long URL already exists in the repository, it returns the existing short URL ID.</p>
     * <p>If no mapping exists, it generates a new short URL ID and stores the mapping in the repository and cache.</p>
     *
     * @param longUrl the original long URL to be shortened.
     * @return a unique short URL ID corresponding to the provided long URL.
     */
    @Override
    public String createShortUrlId(String longUrl) {
        String fullLongUrl = addHttpsPrefix(longUrl);
        Optional<UrlsMatchEntity> urlsMatchEntity = urlShortenerRepository.findByLongUrl(fullLongUrl);
        if (urlsMatchEntity.isPresent()) {
            return urlsMatchEntity.get().getShortUrlId();
        }
        return findFreeShortUrlId(fullLongUrl);
    }

    /**
     * Retrieves the long URL corresponding to the given short URL ID.
     *
     * <p>First, it attempts to retrieve the long URL from the cache. If not found in the cache,
     * it fetches the long URL from the repository.</p>
     *
     * @param shortUrlId the unique short URL ID.
     * @return the original long URL associated with the short URL ID, or throws a {@link ShortURLNotFoundException} if not found.
     */
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
