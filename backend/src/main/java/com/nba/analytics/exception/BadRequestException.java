package com.nba.analytics.exception;

/**
 * Bad Request Exception
 * Thrown when the request is invalid
 */
public class BadRequestException extends RuntimeException {
    
    public BadRequestException(String message) {
        super(message);
    }
    
    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
