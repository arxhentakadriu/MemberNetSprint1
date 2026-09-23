package com.membernet.authorization;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SpringDataMembershipRoleRepository
        extends JpaRepository<MembershipRoleEntity, MembershipRoleId> {

    List<MembershipRoleEntity> findAllByMembershipId(
            UUID membershipId
    );

    List<MembershipRoleEntity> findAllByRoleId(UUID roleId);

    boolean existsByMembershipIdAndRoleId(
            UUID membershipId,
            UUID roleId
    );

    @Query("""
            SELECT COUNT(membershipRole)
            FROM MembershipRoleEntity membershipRole,
                 RolePermissionEntity rolePermission,
                 PermissionEntity permission
            WHERE membershipRole.membershipId = :membershipId
              AND rolePermission.roleId = membershipRole.roleId
              AND permission.id = rolePermission.permissionId
              AND UPPER(permission.code) = UPPER(:permissionCode)
            """)
    long countPermissionAssignments(
            @Param("membershipId") UUID membershipId,
            @Param("permissionCode") String permissionCode
    );
}