package com.membernet.association;

import java.time.OffsetDateTime;
import java.util.UUID;

public record AssociationResponse(
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

    public static AssociationResponse from(Association association) {
        return new AssociationResponse(
                association.id(),
                association.name(),
                association.shortName(),
                association.businessId(),
                association.countryCode(),
                association.email(),
                association.status(),
                association.termsAccepted(),
                association.createdAt(),
                association.updatedAt()
        );
    }
}