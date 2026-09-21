package com.membernet.guardianship;

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
@RequestMapping("/api/guardianships")
public class GuardianshipController {

    private final GuardianshipService service;

    public GuardianshipController(
            GuardianshipService service) {

        this.service = service;
    }

    @PostMapping
    public ResponseEntity<GuardianshipResponse> create(
            @Valid @RequestBody CreateGuardianshipRequest request) {

        GuardianshipResponse response = service.create(request);

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
    public GuardianshipResponse findById(
            @PathVariable UUID id) {

        return service.findById(id);
    }

    @GetMapping("/guardian/{guardianId}")
    public List<GuardianshipResponse> findByGuardian(
            @PathVariable UUID guardianId) {

        return service.findByGuardian(guardianId);
    }

    @GetMapping("/child/{childId}")
    public List<GuardianshipResponse> findByChild(
            @PathVariable UUID childId) {

        return service.findByChild(childId);
    }

    @PatchMapping("/{id}/status")
    public GuardianshipResponse changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody
            UpdateGuardianshipStatusRequest request) {

        return service.changeStatus(id, request.status());
    }
}