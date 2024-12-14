package com.urlshortener.exception;

public class ShortURLGenerationException extends RuntimeException {
    public ShortURLGenerationException(String message) {
        super(message);
    }
}
