package com.yuliiaskrypnyk.backend.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName) {
        super("Requested " + resourceName + " was not found.");
    }
}