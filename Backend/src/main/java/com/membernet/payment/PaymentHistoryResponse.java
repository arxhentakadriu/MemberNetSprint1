package com.membernet.payment;

import java.time.OffsetDateTime;
import java.util.UUID;

public record PaymentHistoryResponse(
        UUID id,
        UUID paymentObligationId,
        PaymentStatus previousStatus,
        PaymentStatus newStatus,
        OffsetDateTime changedAt
) {
}