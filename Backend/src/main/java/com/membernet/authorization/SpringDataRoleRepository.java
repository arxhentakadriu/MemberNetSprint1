package com.membernet.authorization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRoleRepository
        extends JpaRepository<RoleEntity, UUID> {

    Optional<RoleEntity> findByAssociationIdAndCodeIgnoreCase(
            UUID associationId,
            String code
    );

    List<RoleEntity> findAllByAssociationId(UUID associationId);

    boolean existsByAssociationIdAndCodeIgnoreCase(
            UUID associationId,
            String code
    );
}