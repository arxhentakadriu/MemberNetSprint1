package com.membernet.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import com.membernet.association.AssociationEntity;
import com.membernet.membership.MembershipEntity;
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
@Table(name = "payment_obligations")
public class PaymentObligationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_account_id", nullable = false)
    private UserAccountEntity userAccount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "association_id", nullable = false)
    private AssociationEntity association;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "membership_id")
    private MembershipEntity membership;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode;

    @Column(
            name = "payment_reference",
            nullable = false,
            unique = true,
            length = 100
    )
    private String paymentReference;

    @Column(length = 500)
    private String description;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PaymentStatus status;

    @Column(name = "paid_at")
    private OffsetDateTime paidAt;

    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    protected PaymentObligationEntity() {
        // Required by JPA.
    }

    public PaymentObligationEntity(
            UserAccountEntity userAccount,
            AssociationEntity association,
            MembershipEntity membership,
            BigDecimal amount,
            String currencyCode,
            String paymentReference,
            String description,
            LocalDate dueDate) {

        this.userAccount = userAccount;
        this.association = association;
        this.membership = membership;
        this.amount = amount;
        this.currencyCode = currencyCode;
        this.paymentReference = paymentReference;
        this.description = description;
        this.dueDate = dueDate;
        this.status = PaymentStatus.OPEN;
    }

    @PrePersist
    void beforeInsert() {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);

        if (status == null) {
            status = PaymentStatus.OPEN;
        }

        if (currencyCode == null || currencyCode.isBlank()) {
            currencyCode = "EUR";
        }

        currencyCode = currencyCode.trim().toUpperCase();
        paymentReference = paymentReference.trim();

        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void beforeUpdate() {
        updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }

    public void changeStatus(PaymentStatus newStatus) {
        if (newStatus == null) {
            throw new IllegalArgumentException(
                    "Payment status is required."
            );
        }

        this.status = newStatus;

        if (newStatus == PaymentStatus.PAID) {
            this.paidAt = OffsetDateTime.now(ZoneOffset.UTC);
        } else {
            this.paidAt = null;
        }
    }

    public UUID getId() {
        return id;
    }

    public UserAccountEntity getUserAccount() {
        return userAccount;
    }

    public AssociationEntity getAssociation() {
        return association;
    }

    public MembershipEntity getMembership() {
        return membership;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public OffsetDateTime getPaidAt() {
        return paidAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getUpdatedAt() {
        return updatedAt;
    }
}