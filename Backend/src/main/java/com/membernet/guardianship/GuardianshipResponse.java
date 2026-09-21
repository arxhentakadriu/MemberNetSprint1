package com.membernet.guardianship;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record GuardianshipResponse(
        UUID id,

        UUID guardianUserAccountId,
        String guardianEmail,
        String guardianName,

        UUID childUserAccountId,
        String childEmail,
        String childName,

        GuardianshipStatus status,

        LocalDate validFrom,
        LocalDate validUntil,

        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}