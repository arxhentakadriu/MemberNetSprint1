package com.membernet.auth;

import java.util.UUID;

import com.membernet.user.AccountStatus;

public record SessionResponse(
        boolean authenticated,
        UUID userAccountId,
        String loginEmail,
        String displayName,
        AccountStatus accountStatus,
        String homePage) {

    public static SessionResponse from(LoginResponse loginResponse) {
        return new SessionResponse(
                true,
                loginResponse.userAccountId(),
                loginResponse.loginEmail(),
                loginResponse.displayName(),
                loginResponse.accountStatus(),
                loginResponse.homePage()
        );
    }
}