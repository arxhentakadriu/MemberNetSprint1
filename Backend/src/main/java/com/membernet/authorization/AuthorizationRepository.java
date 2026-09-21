package com.membernet.authorization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorizationRepository {

    Role saveRole(RoleEntity role);

    Permission savePermission(PermissionEntity permission);

    Optional<Role> findRoleById(UUID roleId);

    Optional<Permission> findPermissionById(UUID permissionId);

    List<Role> findRolesByAssociationId(UUID associationId);

    boolean roleCodeExists(UUID associationId, String code);

    boolean permissionCodeExists(String code);

    void assignRoleToMembership(MembershipRoleEntity membershipRole);

    void assignPermissionToRole(RolePermissionEntity rolePermission);

    boolean membershipHasRole(UUID membershipId, UUID roleId);

    boolean roleHasPermission(UUID roleId, UUID permissionId);
}