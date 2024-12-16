package com.urlshortener.exception;

/**
 * Custom exception thrown when a shortened URL cannot be found.
 */
public class ShortURLNotFoundException extends IllegalArgumentException {

  /**
   * Constructs a new {@code ShortURLNotFoundException} with the specified message.
   *
   * @param message the message describing the error
   */
  public ShortURLNotFoundException(String message) {
    super(message);
  }
}
