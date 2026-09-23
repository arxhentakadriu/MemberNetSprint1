package com.membernet.authorization;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class PostgresAuthorizationRepository
        implements AuthorizationRepository {

    private final SpringDataRoleRepository roles;
    private final SpringDataPermissionRepository permissions;
    private final SpringDataMembershipRoleRepository membershipRoles;
    private final SpringDataRolePermissionRepository rolePermissions;

    public PostgresAuthorizationRepository(
            SpringDataRoleRepository roles,
            SpringDataPermissionRepository permissions,
            SpringDataMembershipRoleRepository membershipRoles,
            SpringDataRolePermissionRepository rolePermissions) {

        this.roles = roles;
        this.permissions = permissions;
        this.membershipRoles = membershipRoles;
        this.rolePermissions = rolePermissions;
    }

    @Override
    public Role saveRole(RoleEntity role) {
        return toRole(roles.save(role));
    }

    @Override
    public Permission savePermission(PermissionEntity permission) {
        return toPermission(permissions.save(permission));
    }

    @Override
    public Optional<Role> findRoleById(UUID roleId) {
        return roles.findById(roleId)
                .map(this::toRole);
    }

    @Override
    public Optional<Permission> findPermissionById(UUID permissionId) {
        return permissions.findById(permissionId)
                .map(this::toPermission);
    }

    @Override
    public List<Role> findRolesByAssociationId(UUID associationId) {
        return roles.findAllByAssociationId(associationId)
                .stream()
                .map(this::toRole)
                .toList();
    }

    @Override
    public boolean roleCodeExists(
            UUID associationId,
            String code) {

        return roles.existsByAssociationIdAndCodeIgnoreCase(
                associationId,
                code
        );
    }

    @Override
    public boolean permissionCodeExists(String code) {
        return permissions.existsByCodeIgnoreCase(code);
    }

    @Override
    public void assignRoleToMembership(
            MembershipRoleEntity membershipRole) {

        membershipRoles.save(membershipRole);
    }

    @Override
    public void assignPermissionToRole(
            RolePermissionEntity rolePermission) {

        rolePermissions.save(rolePermission);
    }

    @Override
    public boolean membershipHasRole(
            UUID membershipId,
            UUID roleId) {

        return membershipRoles.existsByMembershipIdAndRoleId(
                membershipId,
                roleId
        );
    }

    @Override
    public boolean roleHasPermission(
            UUID roleId,
            UUID permissionId) {

        return rolePermissions.existsByRoleIdAndPermissionId(
                roleId,
                permissionId
        );
    }
    @Override
public boolean membershipHasPermission(
        UUID membershipId,
        String permissionCode) {

    return membershipRoles.countPermissionAssignments(
            membershipId,
            permissionCode
    ) > 0;
}

    private Role toRole(RoleEntity entity) {
        return new Role(
                entity.getId(),
                entity.getAssociationId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }

    private Permission toPermission(PermissionEntity entity) {
        return new Permission(
                entity.getId(),
                entity.getCode(),
                entity.getName(),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }
}