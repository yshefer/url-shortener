package com.urlshortener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the URL Shortener application.
 *
 * Initializes and runs the Spring Boot application context.
 */
@SpringBootApplication
public class UrlShortenerApplication {

	/**
	 * Main method to run the application.
	 *
	 * @param args command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication.run(UrlShortenerApplication.class, args);
	}
}
