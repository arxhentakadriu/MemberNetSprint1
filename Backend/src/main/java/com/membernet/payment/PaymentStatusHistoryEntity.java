package com.membernet.payment;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

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
import jakarta.persistence.Table;

@Entity
@Table(name = "payment_status_history")
public class PaymentStatusHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "payment_obligation_id",
            nullable = false
    )
    private PaymentObligationEntity paymentObligation;

    @Enumerated(EnumType.STRING)
    @Column(name = "previous_status", length = 30)
    private PaymentStatus previousStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "new_status", nullable = false, length = 30)
    private PaymentStatus newStatus;

    @Column(name = "changed_at", nullable = false)
    private OffsetDateTime changedAt;

    protected PaymentStatusHistoryEntity() {
        // Required by JPA.
    }

    public PaymentStatusHistoryEntity(
            PaymentObligationEntity paymentObligation,
            PaymentStatus previousStatus,
            PaymentStatus newStatus) {

        this.paymentObligation = paymentObligation;
        this.previousStatus = previousStatus;
        this.newStatus = newStatus;
    }

    @PrePersist
    void beforeInsert() {
        changedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public UUID getId() {
        return id;
    }

    public PaymentObligationEntity getPaymentObligation() {
        return paymentObligation;
    }

    public PaymentStatus getPreviousStatus() {
        return previousStatus;
    }

    public PaymentStatus getNewStatus() {
        return newStatus;
    }

    public OffsetDateTime getChangedAt() {
        return changedAt;
    }
}