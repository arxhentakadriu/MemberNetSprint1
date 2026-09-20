package com.membernet.association;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class PostgresAssociationRepository
        implements AssociationRepository {

    private final SpringDataAssociationRepository repository;

    public PostgresAssociationRepository(
            SpringDataAssociationRepository repository) {

        this.repository = repository;
    }

    @Override
    public Association save(AssociationEntity association) {
        AssociationEntity savedAssociation = repository.save(association);
        return toAssociation(savedAssociation);
    }

    @Override
    public Optional<Association> findById(UUID id) {
        return repository.findById(id)
                .map(this::toAssociation);
    }

    @Override
    public Optional<Association> findByShortName(String shortName) {
        return repository.findByShortNameIgnoreCase(shortName)
                .map(this::toAssociation);
    }

    @Override
    public Optional<Association> findByBusinessId(String businessId) {
        if (businessId == null || businessId.isBlank()) {
            return Optional.empty();
        }

        return repository.findByBusinessIdIgnoreCase(businessId)
                .map(this::toAssociation);
    }

    @Override
    public List<Association> findAll() {
        return repository.findAll()
                .stream()
                .map(this::toAssociation)
                .toList();
    }

    @Override
    public boolean existsByShortName(String shortName) {
        return repository.existsByShortNameIgnoreCase(shortName);
    }

    @Override
    public boolean existsByBusinessId(String businessId) {
        if (businessId == null || businessId.isBlank()) {
            return false;
        }

        return repository.existsByBusinessIdIgnoreCase(businessId);
    }

    private Association toAssociation(AssociationEntity entity) {
        return new Association(
                entity.getId(),
                entity.getName(),
                entity.getShortName(),
                entity.getBusinessId(),
                entity.getCountryCode(),
                entity.getEmail(),
                entity.getStatus(),
                entity.isTermsAccepted(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}