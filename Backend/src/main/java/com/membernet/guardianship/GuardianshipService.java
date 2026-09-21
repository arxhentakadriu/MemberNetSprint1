package com.membernet.guardianship;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.membernet.user.SpringDataUserAccountRepository;
import com.membernet.user.UserAccountEntity;

@Service
@Transactional
public class GuardianshipService {

    private final GuardianshipRelationshipRepository guardianships;
    private final SpringDataUserAccountRepository users;

    public GuardianshipService(
            GuardianshipRelationshipRepository guardianships,
            SpringDataUserAccountRepository users) {

        this.guardianships = guardianships;
        this.users = users;
    }

    public GuardianshipResponse create(
            CreateGuardianshipRequest request) {

        validateRequest(request);

        UserAccountEntity guardian = users
                .findById(request.guardianUserAccountId())
                .orElseThrow(
                        () -> new UserAccountNotFoundException("Guardian")
                );

        UserAccountEntity child = users
                .findById(request.childUserAccountId())
                .orElseThrow(
                        () -> new UserAccountNotFoundException("Child")
                );

        if (guardianships.existsByGuardian_IdAndChild_Id(
                guardian.getId(),
                child.getId())) {

            throw new DuplicateGuardianshipException();
        }

        GuardianshipRelationshipEntity relationship =
                new GuardianshipRelationshipEntity(
                        guardian,
                        child,
                        request.validFrom(),
                        request.validUntil()
                );

        return toResponse(guardianships.save(relationship));
    }

    @Transactional(readOnly = true)
    public GuardianshipResponse findById(UUID id) {
        return guardianships
                .findById(id)
                .map(this::toResponse)
                .orElseThrow(GuardianshipNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<GuardianshipResponse> findByGuardian(
            UUID guardianId) {

        if (!users.existsById(guardianId)) {
            throw new UserAccountNotFoundException("Guardian");
        }

        return guardianships
                .findAllByGuardian_Id(guardianId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<GuardianshipResponse> findByChild(
            UUID childId) {

        if (!users.existsById(childId)) {
            throw new UserAccountNotFoundException("Child");
        }

        return guardianships
                .findAllByChild_Id(childId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public GuardianshipResponse changeStatus(
            UUID id,
            GuardianshipStatus newStatus) {

        GuardianshipRelationshipEntity relationship =
                guardianships
                        .findById(id)
                        .orElseThrow(
                                GuardianshipNotFoundException::new
                        );

        relationship.changeStatus(newStatus);

        return toResponse(guardianships.save(relationship));
    }

    private void validateRequest(CreateGuardianshipRequest request) {
        if (request.guardianUserAccountId()
                .equals(request.childUserAccountId())) {

            throw new InvalidGuardianshipException(
                    "A user account cannot be its own guardian."
            );
        }

        LocalDate validFrom = request.validFrom();
        LocalDate validUntil = request.validUntil();

        if (validUntil != null
                && validUntil.isBefore(validFrom)) {

            throw new InvalidGuardianshipException(
                    "Valid-until date cannot be before "
                    + "the valid-from date."
            );
        }
    }

    private GuardianshipResponse toResponse(
            GuardianshipRelationshipEntity relationship) {

        UserAccountEntity guardian = relationship.getGuardian();
        UserAccountEntity child = relationship.getChild();

        return new GuardianshipResponse(
                relationship.getId(),

                guardian.getId(),
                guardian.getLoginEmail(),
                fullName(guardian),

                child.getId(),
                child.getLoginEmail(),
                fullName(child),

                relationship.getStatus(),

                relationship.getValidFrom(),
                relationship.getValidUntil(),

                relationship.getCreatedAt(),
                relationship.getUpdatedAt()
        );
    }

    private String fullName(UserAccountEntity user) {
        return user.getFirstName() + " " + user.getLastName();
    }
}