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
@Table(name = "role_permissions")
@IdClass(RolePermissionId.class)
public class RolePermissionEntity {

    @Id
    @Column(name = "role_id", nullable = false)
    private UUID roleId;

    @Id
    @Column(name = "permission_id", nullable = false)
    private UUID permissionId;

    @Column(name = "assigned_at", nullable = false, updatable = false)
    private OffsetDateTime assignedAt;

    protected RolePermissionEntity() {
        // Required by JPA.
    }

    public RolePermissionEntity(
            UUID roleId,
            UUID permissionId) {

        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    @PrePersist
    void beforeInsert() {
        assignedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public UUID getRoleId() {
        return roleId;
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    public OffsetDateTime getAssignedAt() {
        return assignedAt;
    }
}