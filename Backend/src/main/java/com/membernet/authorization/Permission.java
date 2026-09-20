package com.membernet.authorization;

import java.time.OffsetDateTime;
import java.util.UUID;

public record Permission(
        UUID id,
        String code,
        String name,
        String description,
        OffsetDateTime createdAt) {
}