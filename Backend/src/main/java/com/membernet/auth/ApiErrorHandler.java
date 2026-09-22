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
import com.membernet.authorization.AuthorizationForbiddenException;
import com.membernet.authorization.AuthorizationNotFoundException;
import com.membernet.authorization.AuthorizationValidationException;
import com.membernet.guardianship.DuplicateGuardianshipException;
import com.membernet.guardianship.GuardianshipNotFoundException;
import com.membernet.guardianship.InvalidGuardianshipException;
import com.membernet.guardianship.UserAccountNotFoundException;
import com.membernet.membership.DuplicateMembershipException;
import com.membernet.membership.InvalidMembershipException;
import com.membernet.membership.MembershipNotFoundException;
import com.membernet.payment.DuplicatePaymentReferenceException;
import com.membernet.payment.InvalidPaymentException;
import com.membernet.payment.PaymentNotFoundException;
import com.membernet.payment.PaymentResourceNotFoundException;

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
        @ExceptionHandler(AuthorizationForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    Map<String, String> authorizationForbidden(
            AuthorizationForbiddenException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(DuplicateGuardianshipException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> duplicateGuardianship(
            DuplicateGuardianshipException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(InvalidGuardianshipException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> invalidGuardianship(
            InvalidGuardianshipException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(GuardianshipNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> guardianshipNotFound(
            GuardianshipNotFoundException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(UserAccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> userAccountNotFound(
            UserAccountNotFoundException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(DuplicatePaymentReferenceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    Map<String, String> duplicatePaymentReference(
            DuplicatePaymentReferenceException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(InvalidPaymentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    Map<String, String> invalidPayment(
            InvalidPaymentException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(PaymentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> paymentNotFound(
            PaymentNotFoundException error) {

        return Map.of("message", error.getMessage());
    }

    @ExceptionHandler(PaymentResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> paymentResourceNotFound(
            PaymentResourceNotFoundException error) {

        return Map.of("message", error.getMessage());
    }
    @ExceptionHandler(AuthenticationRequiredException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    Map<String, String> authenticationRequired(
            AuthenticationRequiredException error) {

        return Map.of("message", error.getMessage());
    }
}