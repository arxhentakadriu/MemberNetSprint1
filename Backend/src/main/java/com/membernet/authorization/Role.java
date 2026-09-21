package com.membernet.authorization;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Role(
        UUID id,
        UUID associationId,
        String code,
        String name,
        String description,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}