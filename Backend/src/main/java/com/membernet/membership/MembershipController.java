package com.membernet.membership;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private final MembershipService service;

    public MembershipController(MembershipService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<MembershipResponse> create(
            @Valid @RequestBody CreateMembershipRequest request) {

        MembershipResponse response = service.create(request);

        return ResponseEntity
                .created(URI.create("/api/memberships/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public MembershipResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping("/user/{userAccountId}")
    public List<MembershipResponse> findByUser(
            @PathVariable UUID userAccountId) {

        return service.findByUser(userAccountId);
    }

    @GetMapping("/association/{associationId}")
    public List<MembershipResponse> findByAssociation(
            @PathVariable UUID associationId) {

        return service.findByAssociation(associationId);
    }
}