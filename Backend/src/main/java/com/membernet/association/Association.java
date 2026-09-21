package com.membernet.association;

import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Domain representation of an association.
 *
 * This record is independent from JPA and is used by the
 * application and business layers.
 */
public record Association(
        UUID id,
        String name,
        String shortName,
        String businessId,
        String countryCode,
        String email,
        AssociationStatus status,
        boolean termsAccepted,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {

    public boolean isActive() {
        return status == AssociationStatus.ACTIVE;
    }
}