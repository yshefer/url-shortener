package com.urlshortener.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Global exception handler for handling exceptions across the entire application.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles {@link IllegalArgumentException} exceptions, typically thrown when invalid input is provided.
     *
     * @param exception the {@code IllegalArgumentException} that was thrown
     * @param request is the HTTP request
     * @return a response with HTTP 400 (Bad Request) status
     *
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleShortURLNotFoundException(IllegalArgumentException exception,
                                                                               HttpServletRequest request) {
        return getResponseEntity(request, HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    /**
     * Handles generic {@link Exception} types, capturing unexpected server errors.
     *
     * @param exception the exception that was thrown
     * @param request   is the HTTP request
     * @return a response with HTTP 500 (Internal Server Error) status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception exception, HttpServletRequest request) {
        return getResponseEntity(request, HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }

    private ResponseEntity<Map<String, Object>> getResponseEntity(HttpServletRequest request, HttpStatus httpStatus, String message) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("error", httpStatus.getReasonPhrase());
        errorResponse.put("message", message);
        errorResponse.put("path", request.getRequestURI());
        return new ResponseEntity<>(errorResponse, httpStatus);
    }
}