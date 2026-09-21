package com.membernet.user;

import java.util.UUID;

public record UserAccount(
        UUID id,
        String loginEmail,
        String firstName,
        String lastName,
        AccountStatus accountStatus) {

    public String displayName() {
        return firstName + " " + lastName;
    }

    public boolean isActive() {
        return accountStatus == AccountStatus.ACTIVE;
    }
}