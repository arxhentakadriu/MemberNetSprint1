package com.membernet.user;

import java.util.Optional;
import java.util.UUID;

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
    public Optional<UserAccount> findById(UUID id) {
        return repository.findById(id)
                .map(this::toUserAccount);
    }

    @Override
    public Optional<UserAccount> findByLoginEmail(String loginEmail) {
        return repository.findByLoginEmailIgnoreCase(loginEmail)
                .map(this::toUserAccount);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public boolean credentialsMatch(
            String loginEmail,
            String password) {

        return repository.findByLoginEmailIgnoreCase(loginEmail)
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
}