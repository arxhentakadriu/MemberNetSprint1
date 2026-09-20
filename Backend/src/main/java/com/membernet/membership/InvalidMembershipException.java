package com.membernet.membership;

public class InvalidMembershipException extends RuntimeException {

    public InvalidMembershipException(String message) {
        super(message);
    }
}