package com.membernet.authorization;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreatePermissionRequest(

        @NotBlank(message = "Permission code is required.")
        @Size(max = 100, message = "Permission code must not exceed 100 characters.")
        String code,

        @NotBlank(message = "Permission name is required.")
        @Size(max = 150, message = "Permission name must not exceed 150 characters.")
        String name,

        @Size(max = 500, message = "Description must not exceed 500 characters.")
        String description) {
}