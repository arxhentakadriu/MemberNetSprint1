package com.membernet.membership;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MembershipRepository {

    Membership save(MembershipEntity membership);

    Optional<Membership> findById(UUID id);

    Optional<Membership> findByUserAndAssociation(
            UUID userAccountId,
            UUID associationId
    );

    List<Membership> findByUserAccountId(UUID userAccountId);

    List<Membership> findByAssociationId(UUID associationId);

    boolean existsByUserAndAssociation(
            UUID userAccountId,
            UUID associationId
    );
}