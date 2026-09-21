package com.membernet.authorization;

public class AuthorizationNotFoundException extends RuntimeException {

    public AuthorizationNotFoundException(String message) {
        super(message);
    }
}