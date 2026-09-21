package com.membernet.guardianship;

public class UserAccountNotFoundException extends RuntimeException {

    public UserAccountNotFoundException(String accountType) {
        super(accountType + " user account was not found.");
    }
}