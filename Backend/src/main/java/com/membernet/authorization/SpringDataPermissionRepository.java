package com.membernet.authorization;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataPermissionRepository
        extends JpaRepository<PermissionEntity, UUID> {

    Optional<PermissionEntity> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);
}