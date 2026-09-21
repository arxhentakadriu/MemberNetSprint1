package com.membernet.authorization;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataRolePermissionRepository
        extends JpaRepository<RolePermissionEntity, RolePermissionId> {

    List<RolePermissionEntity> findAllByRoleId(UUID roleId);

    List<RolePermissionEntity> findAllByPermissionId(UUID permissionId);

    boolean existsByRoleIdAndPermissionId(
            UUID roleId,
            UUID permissionId
    );
}