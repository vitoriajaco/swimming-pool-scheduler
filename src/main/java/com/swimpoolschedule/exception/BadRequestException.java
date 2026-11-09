package com.swimpoolschedule.exception;

/**
 * Custom exception representing a 400 Bad Request in domain operations.
 */
public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }

    public BadRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}

