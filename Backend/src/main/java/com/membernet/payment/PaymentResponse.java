package com.membernet.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PaymentResponse(
        UUID id,

        UUID userAccountId,
        String userEmail,
        String userName,

        UUID associationId,
        String associationName,

        UUID membershipId,

        BigDecimal amount,
        String currencyCode,

        String paymentReference,
        String description,

        LocalDate dueDate,
        PaymentStatus status,
        OffsetDateTime paidAt,

        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}