package com.urlshortener.service;

import com.urlshortener.entity.UrlsMatchEntity;
import com.urlshortener.exception.ShortURLGenerationException;
import com.urlshortener.exception.ShortURLNotFoundException;
import com.urlshortener.repository.UrlShortenerRepository;
import com.urlshortener.utils.RandomStringGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;

class UrlShortenerServiceImplTest {

    @InjectMocks
    private UrlShortenerServiceImpl urlShortenerService;

    @Mock
    private UrlShortenerRepository urlShortenerRepository;

    @Mock
    private CacheService cacheService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createShortUrlId() {
        String longUrl = "https://example.com";
        String shortUrlId = "abc12";
        int shortIdLength = 5;

        urlShortenerService.setRetryNumber(10);
        urlShortenerService.setShortIdLength(shortIdLength);

        when(urlShortenerRepository.findByLongUrl(longUrl)).thenReturn(Optional.empty());

        try (MockedStatic<RandomStringGenerator> stringGeneratorMockedStatic = Mockito.mockStatic(RandomStringGenerator.class)) {
            stringGeneratorMockedStatic.when(() -> RandomStringGenerator.generateString(shortIdLength))
                    .thenReturn(shortUrlId);
            String result = urlShortenerService.createShortUrlId(longUrl);
            assertEquals(shortUrlId, result);
        }
    }

    @Test
    void createShortUrlIdWithoutProtocol() {
        String longUrl = "example.com";
        String shortUrlId = "abc12";
        int shortIdLength = 5;

        urlShortenerService.setRetryNumber(10);
        urlShortenerService.setShortIdLength(shortIdLength);

        when(urlShortenerRepository.findByLongUrl(longUrl)).thenReturn(Optional.empty());

        try (MockedStatic<RandomStringGenerator> stringGeneratorMockedStatic = Mockito.mockStatic(RandomStringGenerator.class)) {
            stringGeneratorMockedStatic.when(() -> RandomStringGenerator.generateString(shortIdLength))
                    .thenReturn(shortUrlId);
            String result = urlShortenerService.createShortUrlId(longUrl);
            assertEquals(shortUrlId, result);
        }
    }

    @Test
    void createShortUrlWhenUrlExists() {
        String longUrl = "https://example.com";
        String shortUrlId = "abc12";

        when(urlShortenerRepository.findByLongUrl(longUrl)).thenReturn(Optional.of(new UrlsMatchEntity(shortUrlId, longUrl)));

        String result = urlShortenerService.createShortUrlId(longUrl);
        assertEquals(shortUrlId, result);
    }

    @Test
    void createShortUrlIdWhenUrlGenerationException() {
        String longUrl = "https://example.com";
        String shortUrlId = "abc12";
        int shortIdLength = 5;

        urlShortenerService.setRetryNumber(10);
        urlShortenerService.setShortIdLength(shortIdLength);

        when(urlShortenerRepository.findByLongUrl(longUrl)).thenReturn(Optional.empty());
        doThrow(new DataIntegrityViolationException("Constraint violation"))
                .when(urlShortenerRepository)
                .insert(shortUrlId, longUrl);

        try (MockedStatic<RandomStringGenerator> stringGeneratorMockedStatic = Mockito.mockStatic(RandomStringGenerator.class)) {
            stringGeneratorMockedStatic.when(() -> RandomStringGenerator.generateString(shortIdLength))
                    .thenReturn(shortUrlId);
            assertThrows(ShortURLGenerationException.class, () -> urlShortenerService.createShortUrlId(longUrl));
        }
    }

    @Test
    void getLongUrl() {
        String longUrl = "https://example.com";
        String shortUrlId = "abc12";

        when(cacheService.getLongUrl(shortUrlId)).thenReturn(longUrl);
        when(urlShortenerRepository.findById(shortUrlId)).thenReturn(Optional.of(new UrlsMatchEntity(shortUrlId, longUrl)));

        String result = urlShortenerService.getLongUrl(shortUrlId);
        assertEquals(longUrl, result);
    }

    @Test
    void getCachedLongUrl() {
        String longUrl = "https://example.com";
        String shortUrlId = "abc12";

        when(cacheService.getLongUrl(shortUrlId)).thenReturn(longUrl);

        String result = urlShortenerService.getLongUrl(shortUrlId);
        assertEquals(longUrl, result);
    }

    @Test
    void getLongUrlWhenShortURLIsAbsent() {
        String shortUrlId = "abc12";

        when(cacheService.getLongUrl(shortUrlId)).thenReturn(null);
        when(urlShortenerRepository.findById(shortUrlId)).thenReturn(Optional.empty());

        assertThrows(ShortURLNotFoundException.class, () -> urlShortenerService.getLongUrl(shortUrlId));
    }
}