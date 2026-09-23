package com.membernet.authorization;
public class AuthorizationForbiddenException

        extends RuntimeException {

    public AuthorizationForbiddenException(String message) {
        super(message);
    }
}