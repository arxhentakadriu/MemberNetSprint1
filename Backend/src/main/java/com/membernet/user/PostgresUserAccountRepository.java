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
    public Optional<UserAccount> findByUsername(String username) {
        return repository
                .findByUsernameIgnoreCase(username)
                .map(this::toUserAccount);
    }

    @Override
    public boolean credentialsMatch(
            String username,
            String password) {

        return repository
                .findByUsernameIgnoreCase(username)
                .map(user -> passwordEncoder.matches(
                        password,
                        user.getPasswordHash()
                ))
                .orElse(false);
    }

    private UserAccount toUserAccount(UserAccountEntity entity) {
        return new UserAccount(
                entity.getUsername(),
                entity.getDisplayName(),
                entity.getMemberId(),
                entity.getRoles()
        );
    }
}