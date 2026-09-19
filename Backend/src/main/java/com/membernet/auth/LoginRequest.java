package com.membernet.auth;

import com.fasterxml.jackson.annotation.JsonAlias;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

        @JsonAlias("username")
        @NotBlank(message = "Login email is required.")
        @Email(message = "Login email must be valid.")
        String loginEmail,

        @NotBlank(message = "Password is required.")
        String password) {
}