package com.membernet.authorization;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AssignPermissionRequest(

        @NotNull(message = "Role ID is required.")
        UUID roleId,

        @NotNull(message = "Permission ID is required.")
        UUID permissionId) {
}