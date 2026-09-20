package com.membernet.authorization;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMembershipRoleRepository
        extends JpaRepository<MembershipRoleEntity, MembershipRoleId> {

    List<MembershipRoleEntity> findAllByMembershipId(UUID membershipId);

    List<MembershipRoleEntity> findAllByRoleId(UUID roleId);

    boolean existsByMembershipIdAndRoleId(
            UUID membershipId,
            UUID roleId
    );
}