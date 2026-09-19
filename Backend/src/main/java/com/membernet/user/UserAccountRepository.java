package com.membernet.user;

import java.util.Optional;

public interface UserAccountRepository {

    Optional<UserAccount> findByLoginEmail(String loginEmail);

    boolean credentialsMatch(String loginEmail, String password);
}