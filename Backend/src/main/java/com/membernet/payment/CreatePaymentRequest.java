package com.membernet.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreatePaymentRequest(

        @NotNull(message = "User account ID is required.")
        UUID userAccountId,

        @NotNull(message = "Association ID is required.")
        UUID associationId,

        UUID membershipId,

        @NotNull(message = "Payment amount is required.")
        @Positive(message = "Payment amount must be greater than zero.")
        BigDecimal amount,

        @NotBlank(message = "Currency code is required.")
        @Pattern(
                regexp = "^[A-Za-z]{3}$",
                message = "Currency code must contain three letters."
        )
        String currencyCode,

        @NotBlank(message = "Payment reference is required.")
        @Size(
                max = 100,
                message = "Payment reference cannot exceed 100 characters."
        )
        String paymentReference,

        @Size(
                max = 500,
                message = "Description cannot exceed 500 characters."
        )
        String description,

        @NotNull(message = "Due date is required.")
        LocalDate dueDate
) {
}