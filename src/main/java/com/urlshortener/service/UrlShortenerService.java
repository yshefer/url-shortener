package com.urlshortener.service;

/**
 * Interface for URL shortener service providing methods to create short URLs and retrieve long URLs.
 */
public interface UrlShortenerService {

    /**
     * Creates a short URL ID for the given long URL.
     *
     * <p>The implementation should generate a unique short URL ID that can later be used to retrieve the original long URL.</p>
     *
     * @param longUrl the original long URL to be shortened.
     * @return a unique short URL ID corresponding to the given long URL.
     */
    String createShortUrlId(String longUrl);

    /**
     * Retrieves the original long URL corresponding to the given short URL ID.
     *
     * @param shortUrlId the unique short URL ID.
     * @return the original long URL associated with the short URL ID.
     */
    String getLongUrl(String shortUrlId);
}
