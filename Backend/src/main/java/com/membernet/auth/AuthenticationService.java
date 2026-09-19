package com.membernet.auth;

import org.springframework.stereotype.Service;

import com.membernet.user.UserAccount;
import com.membernet.user.UserAccountRepository;

@Service
public class AuthenticationService {

    private final UserAccountRepository accounts;

    public AuthenticationService(UserAccountRepository accounts) {
        this.accounts = accounts;
    }

    public LoginResponse authenticate(LoginRequest request) {
        String loginEmail = request.loginEmail().trim().toLowerCase();

        if (!accounts.credentialsMatch(loginEmail, request.password())) {
            throw new InvalidCredentialsException();
        }

        UserAccount account = accounts
                .findByLoginEmail(loginEmail)
                .filter(UserAccount::isActive)
                .orElseThrow(InvalidCredentialsException::new);

        return new LoginResponse(
                "Login successful. Welcome, " + account.displayName() + ".",
                account.id(),
                account.loginEmail(),
                account.displayName(),
                account.accountStatus(),
                "MemberNet home"
        );
    }
}