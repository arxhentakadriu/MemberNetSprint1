package com.membernet.user;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class PostgresUserAccountRepository
        implements UserAccountRepository {

    private final SpringDataUserAccountRepository repository;
    private final PasswordEncoder passwordEncoder;

    public PostgresUserAccountRepository(
            SpringDataUserAccountRepository repository,
            PasswordEncoder passwordEncoder) {

        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<UserAccount> findByLoginEmail(String loginEmail) {
        return repository
                .findByLoginEmailIgnoreCase(normalizeEmail(loginEmail))
                .map(this::toUserAccount);
    }

    @Override
    public boolean credentialsMatch(
            String loginEmail,
            String password) {

        return repository
                .findByLoginEmailIgnoreCase(normalizeEmail(loginEmail))
                .filter(user -> user.getAccountStatus() == AccountStatus.ACTIVE)
                .map(user -> passwordEncoder.matches(
                        password,
                        user.getPasswordHash()
                ))
                .orElse(false);
    }

    private UserAccount toUserAccount(UserAccountEntity entity) {
        return new UserAccount(
                entity.getId(),
                entity.getLoginEmail(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getAccountStatus()
        );
    }

    private String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }
}