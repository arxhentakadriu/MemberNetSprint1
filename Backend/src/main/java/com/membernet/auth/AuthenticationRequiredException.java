package com.membernet.auth;

public class AuthenticationRequiredException
        extends RuntimeException {

    public AuthenticationRequiredException() {
        super("Authentication is required.");
    }
}