package com.membernet.guardianship;

import java.time.LocalDate;
import java.util.UUID;

import jakarta.validation.constraints.NotNull;

public record CreateGuardianshipRequest(

        @NotNull(message = "Guardian user account ID is required.")
        UUID guardianUserAccountId,

        @NotNull(message = "Child user account ID is required.")
        UUID childUserAccountId,

        @NotNull(message = "Valid-from date is required.")
        LocalDate validFrom,

        LocalDate validUntil
) {
}