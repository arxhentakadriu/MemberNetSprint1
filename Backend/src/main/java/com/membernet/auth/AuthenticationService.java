package com.membernet.auth;

import com.membernet.user.Role;
import com.membernet.user.UserAccount;
import com.membernet.user.UserAccountRepository;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    private final UserAccountRepository accounts;
    public AuthenticationService(UserAccountRepository accounts) { this.accounts = accounts; }

    public LoginResponse authenticate(LoginRequest request) {
        if (!accounts.credentialsMatch(request.username(), request.password())) {
            throw new InvalidCredentialsException();
        }
        UserAccount account = accounts.findByUsername(request.username()).orElseThrow(InvalidCredentialsException::new);
        String homePage = account.roles().contains(Role.ADMIN) ? "Administrator dashboard" : "Member home";
        return new LoginResponse("Login successful. Welcome, " + account.displayName() + ".", account.username(),
                account.displayName(), account.memberId(), account.roles(), homePage);
    }
}
