package com.membernet.authorization;

import java.time.OffsetDateTime;
import java.util.UUID;

public record RoleResponse(
        UUID id,
        UUID associationId,
        String code,
        String name,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public static RoleResponse from(Role role) {
        return new RoleResponse(
                role.id(),
                role.associationId(),
                role.code(),
                role.name(),
                role.description(),
                role.createdAt(),
                role.updatedAt()
        );
    }
}