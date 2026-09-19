package com.membernet.auth;

import java.util.UUID;

import com.membernet.user.AccountStatus;

public record LoginResponse(
        String message,
        UUID userAccountId,
        String loginEmail,
        String displayName,
        AccountStatus accountStatus,
        String homePage) {
}