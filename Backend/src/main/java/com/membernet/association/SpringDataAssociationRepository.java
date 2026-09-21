package com.membernet.association;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository used for direct database operations.
 */
public interface SpringDataAssociationRepository
        extends JpaRepository<AssociationEntity, UUID> {

    Optional<AssociationEntity> findByShortNameIgnoreCase(String shortName);

    Optional<AssociationEntity> findByBusinessIdIgnoreCase(String businessId);

    boolean existsByShortNameIgnoreCase(String shortName);

    boolean existsByBusinessIdIgnoreCase(String businessId);
}