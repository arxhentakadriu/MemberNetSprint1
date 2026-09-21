package com.membernet.association;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateAssociationRequest(

        @NotBlank(message = "Association name is required.")
        @Size(max = 200, message = "Association name must not exceed 200 characters.")
        String name,

        @NotBlank(message = "Short name is required.")
        @Size(max = 50, message = "Short name must not exceed 50 characters.")
        String shortName,

        @Size(max = 100, message = "Business ID must not exceed 100 characters.")
        String businessId,

        @NotBlank(message = "Country code is required.")
        @Size(min = 2, max = 2,
              message = "Country code must contain exactly 2 characters.")
        String countryCode,

        @NotBlank(message = "Email is required.")
        @Email(message = "Email must be valid.")
        @Size(max = 255, message = "Email must not exceed 255 characters.")
        String email,

        @AssertTrue(message = "Terms must be accepted.")
        boolean termsAccepted) {
}