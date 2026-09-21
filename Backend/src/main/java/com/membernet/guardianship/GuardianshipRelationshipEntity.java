package com.membernet.guardianship;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import com.membernet.user.UserAccountEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "guardianship_relationships")
public class GuardianshipRelationshipEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "guardian_user_account_id",
            nullable = false
    )
    private UserAccountEntity guardian;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "child_user_account_id",
            nullable = false
    )
    private UserAccountEntity child;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private GuardianshipStatus status;

    @Column(name = "valid_from", nullable = false)
    private LocalDate validFrom;

    @Column(name = "valid_until")
    private LocalDate validUntil;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected GuardianshipRelationshipEntity() {
        // Required by JPA.
    }

    public GuardianshipRelationshipEntity(
            UserAccountEntity guardian,
            UserAccountEntity child,
            LocalDate validFrom,
            LocalDate validUntil) {

        this.guardian = guardian;
        this.child = child;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.status = GuardianshipStatus.PENDING;
    }

    @PrePersist
    void beforeInsert() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        if (status == null) {
            status = GuardianshipStatus.PENDING;
        }

        if (validFrom == null) {
            validFrom = LocalDate.now(ZoneOffset.UTC);
        }

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void beforeUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void changeStatus(GuardianshipStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "Guardianship status is required."
            );
        }

        this.status = newStatus;
    }

    public UUID getId() {
        return id;
    }

    public UserAccountEntity getGuardian() {
        return guardian;
    }

    public UserAccountEntity getChild() {
        return child;
    }

    public GuardianshipStatus getStatus() {
        return status;
    }

    public LocalDate getValidFrom() {
        return validFrom;
    }

    public LocalDate getValidUntil() {
        return validUntil;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}