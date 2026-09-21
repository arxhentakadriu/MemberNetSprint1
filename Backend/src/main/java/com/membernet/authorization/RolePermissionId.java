package com.membernet.authorization;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public class RolePermissionId implements Serializable {

    private UUID roleId;
    private UUID permissionId;

    public RolePermissionId() {
    }

    public RolePermissionId(UUID roleId, UUID permissionId) {
        this.roleId = roleId;
        this.permissionId = permissionId;
    }

    public UUID getRoleId() {
        return roleId;
    }

    public UUID getPermissionId() {
        return permissionId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof RolePermissionId other)) {
            return false;
        }

        return Objects.equals(roleId, other.roleId)
                && Objects.equals(permissionId, other.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, permissionId);
    }
}