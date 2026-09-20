package com.membernet.association;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Persistence port for associations.
 *
 * The application depends on this interface instead of depending
 * directly on Spring Data JPA.
 */
public interface AssociationRepository {

    Association save(AssociationEntity association);

    Optional<Association> findById(UUID id);

    Optional<Association> findByShortName(String shortName);

    Optional<Association> findByBusinessId(String businessId);

    List<Association> findAll();

    boolean existsByShortName(String shortName);

    boolean existsByBusinessId(String businessId);
}