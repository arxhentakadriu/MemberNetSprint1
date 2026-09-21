package com.membernet.authorization;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record AssignRoleRequest(

        @NotNull(message = "Membership ID is required.")
        UUID membershipId,

        @NotNull(message = "Role ID is required.")
        UUID roleId) {
}