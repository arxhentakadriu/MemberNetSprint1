package com.membernet.membership;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

@Repository
public class PostgresMembershipRepository
        implements MembershipRepository {

    private final SpringDataMembershipRepository repository;

    public PostgresMembershipRepository(
            SpringDataMembershipRepository repository) {

        this.repository = repository;
    }

    @Override
    public Membership save(MembershipEntity membership) {
        return toMembership(repository.save(membership));
    }

    @Override
    public Optional<Membership> findById(UUID id) {
        return repository.findById(id)
                .map(this::toMembership);
    }

    @Override
    public Optional<Membership> findByUserAndAssociation(
            UUID userAccountId,
            UUID associationId) {

        return repository
                .findByUserAccountIdAndAssociationId(
                        userAccountId,
                        associationId
                )
                .map(this::toMembership);
    }

    @Override
    public List<Membership> findByUserAccountId(UUID userAccountId) {
        return repository.findAllByUserAccountId(userAccountId)
                .stream()
                .map(this::toMembership)
                .toList();
    }

    @Override
    public List<Membership> findByAssociationId(UUID associationId) {
        return repository.findAllByAssociationId(associationId)
                .stream()
                .map(this::toMembership)
                .toList();
    }

    @Override
    public boolean existsByUserAndAssociation(
            UUID userAccountId,
            UUID associationId) {

        return repository.existsByUserAccountIdAndAssociationId(
                userAccountId,
                associationId
        );
    }

    private Membership toMembership(MembershipEntity entity) {
        return new Membership(
                entity.getId(),
                entity.getUserAccountId(),
                entity.getAssociationId(),
                entity.getStatus(),
                entity.getValidFrom(),
                entity.getValidUntil(),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}