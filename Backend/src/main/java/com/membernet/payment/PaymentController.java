package com.membernet.payment;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support
        .ServletUriComponentsBuilder;

import com.membernet.auth.SessionAuthenticationInterceptor;
import com.membernet.auth.SessionResponse;
import com.membernet.authorization.AuthorizationForbiddenException;
import com.membernet.authorization.AuthorizationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private static final String PAYMENT_MANAGE =
            "PAYMENT_MANAGE";

    private final PaymentService service;
    private final AuthorizationService authorization;

    public PaymentController(
            PaymentService service,
            AuthorizationService authorization) {

        this.service = service;
        this.authorization = authorization;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody CreatePaymentRequest request,
            HttpServletRequest servletRequest) {

        UUID currentUserId =
                currentUserId(servletRequest);

        if (currentUserId != null) {
            authorization.requirePermission(
                    currentUserId,
                    request.associationId(),
                    PAYMENT_MANAGE
            );
        }

        PaymentResponse response =
                service.create(request);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping("/{id}")
    public PaymentResponse findById(
            @PathVariable UUID id,
            HttpServletRequest servletRequest) {

        PaymentResponse payment =
                service.findById(id);

        authorizePaymentRead(
                currentUserId(servletRequest),
                payment
        );

        return payment;
    }

    @GetMapping("/user/{userAccountId}")
    public List<PaymentResponse> findAllByUser(
            @PathVariable UUID userAccountId,
            HttpServletRequest servletRequest) {

        List<PaymentResponse> payments =
                service.findAllByUser(userAccountId);

        authorizePaymentListRead(
                currentUserId(servletRequest),
                userAccountId,
                payments
        );

        return payments;
    }

    @GetMapping("/user/{userAccountId}/open")
    public List<PaymentResponse> findOpenByUser(
            @PathVariable UUID userAccountId,
            HttpServletRequest servletRequest) {

        List<PaymentResponse> payments =
                service.findOpenByUser(userAccountId);

        authorizePaymentListRead(
                currentUserId(servletRequest),
                userAccountId,
                payments
        );

        return payments;
    }

    @PatchMapping("/{id}/status")
    public PaymentResponse changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody
            UpdatePaymentStatusRequest request,
            HttpServletRequest servletRequest) {

        PaymentResponse existingPayment =
                service.findById(id);

        UUID currentUserId =
                currentUserId(servletRequest);

        if (currentUserId != null) {
            authorization.requirePermission(
                    currentUserId,
                    existingPayment.associationId(),
                    PAYMENT_MANAGE
            );
        }

        return service.changeStatus(
                id,
                request.status()
        );
    }

    @GetMapping("/{id}/history")
    public List<PaymentHistoryResponse> findHistory(
            @PathVariable UUID id,
            HttpServletRequest servletRequest) {

        PaymentResponse payment =
                service.findById(id);

        authorizePaymentRead(
                currentUserId(servletRequest),
                payment
        );

        return service.findHistory(id);
    }

    private void authorizePaymentRead(
            UUID currentUserId,
            PaymentResponse payment) {

        if (currentUserId == null
                || currentUserId.equals(
                        payment.userAccountId()
                )) {

            return;
        }

        authorization.requirePermission(
                currentUserId,
                payment.associationId(),
                PAYMENT_MANAGE
        );
    }

    private void authorizePaymentListRead(
            UUID currentUserId,
            UUID requestedUserId,
            List<PaymentResponse> payments) {

        if (currentUserId == null
                || currentUserId.equals(
                        requestedUserId
                )) {

            return;
        }

        if (payments.isEmpty()) {
            throw new AuthorizationForbiddenException(
                    "You cannot access payments "
                            + "for this user."
            );
        }

        payments.stream()
                .map(PaymentResponse::associationId)
                .distinct()
                .forEach(associationId ->
                        authorization.requirePermission(
                                currentUserId,
                                associationId,
                                PAYMENT_MANAGE
                        )
                );
    }

    private UUID currentUserId(
            HttpServletRequest request) {

        HttpSession session =
                request.getSession(false);

        if (session == null) {
            return null;
        }

        Object authenticatedUser =
                session.getAttribute(
                        SessionAuthenticationInterceptor
                                .AUTHENTICATED_USER_ATTRIBUTE
                );

        if (authenticatedUser
                instanceof SessionResponse sessionResponse) {

            return sessionResponse.userAccountId();
        }

        return null;
    }
}