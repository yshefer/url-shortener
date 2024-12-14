package com.urlshortener.exception;

public class ShortURLNotFoundException extends IllegalArgumentException {
  public ShortURLNotFoundException(String message) {
    super(message);
  }
}
