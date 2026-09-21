package com.membernet.user;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataUserAccountRepository
        extends JpaRepository<UserAccountEntity, UUID> {

    Optional<UserAccountEntity> findByLoginEmailIgnoreCase(String loginEmail);

    boolean existsByLoginEmailIgnoreCase(String loginEmail);
}