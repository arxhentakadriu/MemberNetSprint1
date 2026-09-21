package com.membernet.payment;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.membernet.association.AssociationEntity;
import com.membernet.association.SpringDataAssociationRepository;
import com.membernet.membership.MembershipEntity;
import com.membernet.membership.SpringDataMembershipRepository;
import com.membernet.user.SpringDataUserAccountRepository;
import com.membernet.user.UserAccountEntity;

@Service
@Transactional
public class PaymentService {

    private final PaymentObligationRepository payments;
    private final PaymentStatusHistoryRepository history;
    private final SpringDataUserAccountRepository users;
    private final SpringDataAssociationRepository associations;
    private final SpringDataMembershipRepository memberships;

    public PaymentService(
            PaymentObligationRepository payments,
            PaymentStatusHistoryRepository history,
            SpringDataUserAccountRepository users,
            SpringDataAssociationRepository associations,
            SpringDataMembershipRepository memberships) {

        this.payments = payments;
        this.history = history;
        this.users = users;
        this.associations = associations;
        this.memberships = memberships;
    }

    public PaymentResponse create(CreatePaymentRequest request) {
        String normalizedReference =
                request.paymentReference().trim();

        if (payments.existsByPaymentReferenceIgnoreCase(
                normalizedReference)) {

            throw new DuplicatePaymentReferenceException();
        }

        UserAccountEntity user = users
                .findById(request.userAccountId())
                .orElseThrow(
                        () -> new PaymentResourceNotFoundException(
                                "User account"
                        )
                );

        AssociationEntity association = associations
                .findById(request.associationId())
                .orElseThrow(
                        () -> new PaymentResourceNotFoundException(
                                "Association"
                        )
                );

        MembershipEntity membership = null;

        if (request.membershipId() != null) {
            membership = memberships
                    .findById(request.membershipId())
                    .orElseThrow(
                            () -> new PaymentResourceNotFoundException(
                                    "Membership"
                            )
                    );

            validateMembership(
                    membership,
                    user.getId(),
                    association.getId()
            );
        }

        PaymentObligationEntity payment =
                new PaymentObligationEntity(
                        user,
                        association,
                        membership,
                        request.amount(),
                        request.currencyCode(),
                        normalizedReference,
                        request.description(),
                        request.dueDate()
                );

        PaymentObligationEntity saved = payments.save(payment);

        history.save(
                new PaymentStatusHistoryEntity(
                        saved,
                        null,
                        PaymentStatus.OPEN
                )
        );

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PaymentResponse findById(UUID id) {
        return payments
                .findById(id)
                .map(this::toResponse)
                .orElseThrow(PaymentNotFoundException::new);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> findAllByUser(
            UUID userAccountId) {

        if (!users.existsById(userAccountId)) {
            throw new PaymentResourceNotFoundException(
                    "User account"
            );
        }

        return payments
                .findAllByUserAccount_IdOrderByDueDateDesc(
                        userAccountId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> findOpenByUser(
            UUID userAccountId) {

        if (!users.existsById(userAccountId)) {
            throw new PaymentResourceNotFoundException(
                    "User account"
            );
        }

        return payments
                .findAllByUserAccount_IdAndStatusOrderByDueDateAsc(
                        userAccountId,
                        PaymentStatus.OPEN
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PaymentResponse changeStatus(
            UUID id,
            PaymentStatus newStatus) {

        PaymentObligationEntity payment = payments
                .findById(id)
                .orElseThrow(PaymentNotFoundException::new);

        PaymentStatus previousStatus = payment.getStatus();

        validateStatusTransition(previousStatus, newStatus);

        payment.changeStatus(newStatus);

        PaymentObligationEntity saved = payments.save(payment);

        history.save(
                new PaymentStatusHistoryEntity(
                        saved,
                        previousStatus,
                        newStatus
                )
        );

        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<PaymentHistoryResponse> findHistory(
            UUID paymentId) {

        if (!payments.existsById(paymentId)) {
            throw new PaymentNotFoundException();
        }

        return history
                .findAllByPaymentObligation_IdOrderByChangedAtAsc(
                        paymentId
                )
                .stream()
                .map(this::toHistoryResponse)
                .toList();
    }

    private void validateMembership(
            MembershipEntity membership,
            UUID userAccountId,
            UUID associationId) {

        if (!membership.getUserAccountId()
                .equals(userAccountId)) {

            throw new InvalidPaymentException(
                    "Membership does not belong "
                    + "to the selected user account."
            );
        }

        if (!membership.getAssociationId()
                .equals(associationId)) {

            throw new InvalidPaymentException(
                    "Membership does not belong "
                    + "to the selected association."
            );
        }
    }

    private void validateStatusTransition(
            PaymentStatus currentStatus,
            PaymentStatus newStatus) {

        if (newStatus == null) {
            throw new InvalidPaymentException(
                    "Payment status is required."
            );
        }

        if (currentStatus == newStatus) {
            throw new InvalidPaymentException(
                    "Payment already has the requested status."
            );
        }

        if (currentStatus == PaymentStatus.PAID) {
            throw new InvalidPaymentException(
                    "A paid obligation cannot change status."
            );
        }

        if (currentStatus == PaymentStatus.CANCELLED) {
            throw new InvalidPaymentException(
                    "A cancelled obligation cannot change status."
            );
        }

        if (currentStatus == PaymentStatus.OPEN
                && newStatus == PaymentStatus.OPEN) {

            throw new InvalidPaymentException(
                    "Invalid payment status transition."
            );
        }

        if (currentStatus == PaymentStatus.OVERDUE
                && newStatus == PaymentStatus.OPEN) {

            throw new InvalidPaymentException(
                    "An overdue obligation cannot return to OPEN."
            );
        }
    }

    private PaymentResponse toResponse(
            PaymentObligationEntity payment) {

        UserAccountEntity user = payment.getUserAccount();
        AssociationEntity association =
                payment.getAssociation();

        UUID membershipId = payment.getMembership() == null
                ? null
                : payment.getMembership().getId();

        return new PaymentResponse(
                payment.getId(),

                user.getId(),
                user.getLoginEmail(),
                user.getFirstName() + " " + user.getLastName(),

                association.getId(),
                association.getName(),

                membershipId,

                payment.getAmount(),
                payment.getCurrencyCode(),

                payment.getPaymentReference(),
                payment.getDescription(),

                payment.getDueDate(),
                payment.getStatus(),
                payment.getPaidAt(),

                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }

    private PaymentHistoryResponse toHistoryResponse(
            PaymentStatusHistoryEntity entry) {

        return new PaymentHistoryResponse(
                entry.getId(),
                entry.getPaymentObligation().getId(),
                entry.getPreviousStatus(),
                entry.getNewStatus(),
                entry.getChangedAt()
        );
    }
}