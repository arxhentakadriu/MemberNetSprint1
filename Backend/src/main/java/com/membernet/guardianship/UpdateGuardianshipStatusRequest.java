package com.membernet.guardianship;

import jakarta.validation.constraints.NotNull;

public record UpdateGuardianshipStatusRequest(

        @NotNull(message = "Guardianship status is required.")
        GuardianshipStatus status
) {
}