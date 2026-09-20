package com.membernet.membership;

import java.util.List;
import java.util.UUID;

import com.membernet.association.AssociationRepository;
import com.membernet.user.UserAccountRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MembershipService {

    private final MembershipRepository memberships;
    private final UserAccountRepository users;
    private final AssociationRepository associations;

    public MembershipService(
            MembershipRepository memberships,
            UserAccountRepository users,
            AssociationRepository associations) {

        this.memberships = memberships;
        this.users = users;
        this.associations = associations;
    }

    @Transactional
    public MembershipResponse create(CreateMembershipRequest request) {
        validateReferences(request);
        validateDates(request);

        if (memberships.existsByUserAndAssociation(
                request.userAccountId(),
                request.associationId())) {

            throw new DuplicateMembershipException();
        }

        MembershipEntity entity = new MembershipEntity(
                request.userAccountId(),
                request.associationId(),
                MembershipStatus.ACTIVE,
                request.validFrom(),
                request.validUntil()
        );

        return MembershipResponse.from(memberships.save(entity));
    }

    @Transactional(readOnly = true)
    public MembershipResponse findById(UUID id) {
        Membership membership = memberships.findById(id)
                .orElseThrow(() -> new MembershipNotFoundException(id));

        return MembershipResponse.from(membership);
    }

    @Transactional(readOnly = true)
    public List<MembershipResponse> findByUser(UUID userAccountId) {
        return memberships.findByUserAccountId(userAccountId)
                .stream()
                .map(MembershipResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<MembershipResponse> findByAssociation(UUID associationId) {
        return memberships.findByAssociationId(associationId)
                .stream()
                .map(MembershipResponse::from)
                .toList();
    }

    private void validateReferences(CreateMembershipRequest request) {
        if (!users.existsById(request.userAccountId())) {
            throw new InvalidMembershipException(
                    "The selected user account does not exist."
            );
        }

        if (associations.findById(request.associationId()).isEmpty()) {
            throw new InvalidMembershipException(
                    "The selected association does not exist."
            );
        }
    }

    private void validateDates(CreateMembershipRequest request) {
        if (request.validUntil() != null
                && request.validUntil().isBefore(request.validFrom())) {

            throw new InvalidMembershipException(
                    "Valid-until date cannot be before valid-from date."
            );
        }
    }
}