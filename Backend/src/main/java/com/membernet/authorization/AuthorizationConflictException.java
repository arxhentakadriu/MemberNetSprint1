package com.membernet.authorization;

public class AuthorizationConflictException extends RuntimeException {

    public AuthorizationConflictException(String message) {
        super(message);
    }
}