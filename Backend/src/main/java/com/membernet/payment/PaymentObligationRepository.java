package com.membernet.payment;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentObligationRepository
        extends JpaRepository<PaymentObligationEntity, UUID> {

    boolean existsByPaymentReferenceIgnoreCase(
            String paymentReference
    );

    List<PaymentObligationEntity>
            findAllByUserAccount_IdOrderByDueDateDesc(
                    UUID userAccountId
            );

    List<PaymentObligationEntity>
            findAllByUserAccount_IdAndStatusOrderByDueDateAsc(
                    UUID userAccountId,
                    PaymentStatus status
            );
}