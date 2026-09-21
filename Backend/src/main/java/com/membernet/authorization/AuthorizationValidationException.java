package com.membernet.authorization;

public class AuthorizationValidationException extends RuntimeException {

    public AuthorizationValidationException(String message) {
        super(message);
    }
}