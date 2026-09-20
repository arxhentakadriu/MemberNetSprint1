package com.membernet.auth;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.membernet.association.AssociationNotFoundException;
import com.membernet.association.DuplicateAssociationException;
import com.membernet.authorization.AuthorizationConflictException;
import com.membernet.authorization.AuthorizationNotFoundException;
import com.membernet.authorization.AuthorizationValidationException;
import com.membernet.membership.DuplicateMembershipException;
import com.membernet.membership.InvalidMembershipException;
import com.membernet.membership.MembershipNotFoundException;

@RestControllerAdvice
public class ApiErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> validation(
            MethodArgumentNotValidException error) {

        String message = error.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(fieldError -> fieldError.getDefaultMessage())
                .orElse("Request validation failed.");

        return Map.of("message", message);
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Map<String, String> invalidCredentials() {
        return Map.of(
                "message",
                "The username or password is incorrect."
        );
    }

    @ExceptionHandler(DuplicateAssociationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> duplicateAssociation(
            DuplicateAssociationException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(AssociationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> associationNotFound(
            AssociationNotFoundException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(DuplicateMembershipException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> duplicateMembership(
            DuplicateMembershipException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(InvalidMembershipException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> invalidMembership(
            InvalidMembershipException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(MembershipNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> membershipNotFound(
            MembershipNotFoundException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(AuthorizationConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> authorizationConflict(
            AuthorizationConflictException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(AuthorizationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> authorizationNotFound(
            AuthorizationNotFoundException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(AuthorizationValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> authorizationValidation(
            AuthorizationValidationException error) {

        return Map.of("message", error.getMessage());
    }
}