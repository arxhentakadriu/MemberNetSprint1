package com.membernet.membership;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CreateMembershipRequest(

        @NotNull(message = "User account ID is required.")
        UUID userAccountId,

        @NotNull(message = "Association ID is required.")
        UUID associationId,

        @NotNull(message = "Valid-from date is required.")
        LocalDate validFrom,

        LocalDate validUntil) {
}