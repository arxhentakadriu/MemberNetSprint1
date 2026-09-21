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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PaymentResponse> create(
            @Valid @RequestBody CreatePaymentRequest request) {

        PaymentResponse response = service.create(request);

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
            @PathVariable UUID id) {

        return service.findById(id);
    }

    @GetMapping("/user/{userAccountId}")
    public List<PaymentResponse> findAllByUser(
            @PathVariable UUID userAccountId) {

        return service.findAllByUser(userAccountId);
    }

    @GetMapping("/user/{userAccountId}/open")
    public List<PaymentResponse> findOpenByUser(
            @PathVariable UUID userAccountId) {

        return service.findOpenByUser(userAccountId);
    }

    @PatchMapping("/{id}/status")
    public PaymentResponse changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody
            UpdatePaymentStatusRequest request) {

        return service.changeStatus(id, request.status());
    }

    @GetMapping("/{id}/history")
    public List<PaymentHistoryResponse> findHistory(
            @PathVariable UUID id) {

        return service.findHistory(id);
    }
}