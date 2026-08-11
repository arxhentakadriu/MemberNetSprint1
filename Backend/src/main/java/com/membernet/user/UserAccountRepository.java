package com.membernet.user;

import java.util.Optional;

/** Port for account persistence; replace this in-memory adapter with a database implementation later. */
public interface UserAccountRepository {
    Optional<UserAccount> findByUsername(String username);
    boolean credentialsMatch(String username, String password);
}
