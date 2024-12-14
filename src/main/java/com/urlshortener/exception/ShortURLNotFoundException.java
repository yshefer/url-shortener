package com.urlshortener.exception;

public class ShortURLNotFoundException extends RuntimeException {
  public ShortURLNotFoundException(String message) {
    super(message);
  }
}
