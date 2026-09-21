package com.membernet.membership;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record MembershipResponse(
        UUID id,
        UUID userAccountId,
        UUID associationId,
        MembershipStatus status,
        LocalDate validFrom,
        LocalDate validUntil,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public static MembershipResponse from(Membership membership) {
        return new MembershipResponse(
                membership.id(),
                membership.userAccountId(),
                membership.associationId(),
                membership.status(),
                membership.validFrom(),
                membership.validUntil(),
                membership.createdAt(),
                membership.updatedAt()
        );
    }
}