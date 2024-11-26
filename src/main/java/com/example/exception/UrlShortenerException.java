package com.example.exception;

public class UrlShortenerException extends RuntimeException {
    
    public UrlShortenerException(String message, Throwable cause) {
        super(message, cause);
    }
}
