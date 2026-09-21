package com.membernet.guardianship;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GuardianshipRelationshipRepository
        extends JpaRepository<GuardianshipRelationshipEntity, UUID> {

    boolean existsByGuardian_IdAndChild_Id(
            UUID guardianId,
            UUID childId
    );

    Optional<GuardianshipRelationshipEntity>
            findByGuardian_IdAndChild_Id(
                    UUID guardianId,
                    UUID childId
            );

    List<GuardianshipRelationshipEntity>
            findAllByGuardian_Id(UUID guardianId);

    List<GuardianshipRelationshipEntity>
            findAllByChild_Id(UUID childId);
}