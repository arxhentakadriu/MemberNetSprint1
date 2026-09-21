package com.membernet.user;

import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository {

    Optional<UserAccount> findById(UUID id);

    Optional<UserAccount> findByLoginEmail(String loginEmail);

    boolean existsById(UUID id);

    boolean credentialsMatch(
            String loginEmail,
            String password
    );
}