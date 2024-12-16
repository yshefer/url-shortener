package com.urlshortener.exception;

/**
 * Custom exception thrown when there is an error during the generation of a shortened URL.
 */
public class ShortURLGenerationException extends RuntimeException {

    /**
     * Constructs a new {@code ShortURLGenerationException} with the specified message.
     *
     * @param message the message describing the error
     */
    public ShortURLGenerationException(String message) {
        super(message);
    }
}
