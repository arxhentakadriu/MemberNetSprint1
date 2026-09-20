package com.membernet.authorization;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

@Entity
@Table(name = "membership_roles")
@IdClass(MembershipRoleId.class)
public class MembershipRoleEntity {

    @Id
    @Column(name = "membership_id", nullable = false)
    private UUID membershipId;

    @Id
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Column(name = "association_id", nullable = false)
    private UUID associationId;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private OffsetDateTime assignedAt;

    protected MembershipRoleEntity() {
        // Required by JPA.
    }

    public MembershipRoleEntity(
            UUID membershipId,
            UUID roleId,
            UUID associationId) {

        this.membershipId = membershipId;
        this.roleId = roleId;
        this.associationId = associationId;
    }

    @PrePersist
    void beforeInsert() {
        assignedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public UUID getMembershipId() {
        return membershipId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public UUID getAssociationId() {
        return associationId;
    }

    public OffsetDateTime getAssignedAt() {
        return assignedAt;
    }
}