package com.example.mindStreamApplication.Exception;

public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceType, String identifier, Object value) {
        super(String.format("%s already exists with %s: '%s'", resourceType, identifier, value));
    }
}