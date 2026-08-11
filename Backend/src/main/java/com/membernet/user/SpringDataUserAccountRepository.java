package com.membernet.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserAccountRepository
        extends JpaRepository<UserAccountEntity, Long> {

    Optional<UserAccountEntity> findByUsernameIgnoreCase(String username);

    boolean existsByUsernameIgnoreCase(String username);
}