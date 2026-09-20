package com.membernet.authorization;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PermissionResponse(
        UUID id,
        String code,
        String name,
        String description,
        OffsetDateTime createdAt) {

    public static PermissionResponse from(
            Permission permission) {

        return new PermissionResponse(
                permission.id(),
                permission.code(),
                permission.name(),
                permission.description(),
                permission.createdAt()
        );
    }
}