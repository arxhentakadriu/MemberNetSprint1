package com.membernet.association;

import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AssociationService {

    private final AssociationRepository associations;

    public AssociationService(AssociationRepository associations) {
        this.associations = associations;
    }

    @Transactional
    public AssociationResponse create(CreateAssociationRequest request) {
        String shortName = request.shortName().trim().toUpperCase(Locale.ROOT);
        String businessId = normalizeOptional(request.businessId());

        if (associations.existsByShortName(shortName)) {
            throw new DuplicateAssociationException(
                    "An association with this short name already exists."
            );
        }

        if (businessId != null && associations.existsByBusinessId(businessId)) {
            throw new DuplicateAssociationException(
                    "An association with this business ID already exists."
            );
        }

        AssociationEntity entity = new AssociationEntity(
                request.name().trim(),
                shortName,
                businessId,
                request.countryCode().trim().toUpperCase(Locale.ROOT),
                request.email().trim().toLowerCase(Locale.ROOT),
                AssociationStatus.ACTIVE,
                request.termsAccepted()
        );

        return AssociationResponse.from(associations.save(entity));
    }

    @Transactional(readOnly = true)
    public AssociationResponse findById(UUID id) {
        Association association = associations.findById(id)
                .orElseThrow(() -> new AssociationNotFoundException(id));

        return AssociationResponse.from(association);
    }

    @Transactional(readOnly = true)
    public List<AssociationResponse> findAll() {
        return associations.findAll()
                .stream()
                .map(AssociationResponse::from)
                .toList();
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}