package com.membernet.membership;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record Membership(
        UUID id,
        UUID userAccountId,
        UUID associationId,
        MembershipStatus status,
        LocalDate validFrom,
        LocalDate validUntil,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public boolean isActiveOn(LocalDate date) {
        boolean started = !date.isBefore(validFrom);
        boolean notEnded = validUntil == null || !date.isAfter(validUntil);

        return status == MembershipStatus.ACTIVE
                && started
                && notEnded;
    }
}