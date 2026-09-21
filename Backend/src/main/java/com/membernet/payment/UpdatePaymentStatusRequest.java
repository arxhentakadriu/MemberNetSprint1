package com.membernet.payment;

import jakarta.validation.constraints.NotNull;

public record UpdatePaymentStatusRequest(

        @NotNull(message = "Payment status is required.")
        PaymentStatus status
) {
}