package com.membernet.association;

import java.net.URI;
import java.util.List;
import java.util.UUID;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/associations")
public class AssociationController {

    private final AssociationService service;

    public AssociationController(AssociationService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<AssociationResponse> create(
            @Valid @RequestBody CreateAssociationRequest request) {

        AssociationResponse response = service.create(request);

        return ResponseEntity
                .created(URI.create("/api/associations/" + response.id()))
                .body(response);
    }

    @GetMapping("/{id}")
    public AssociationResponse findById(@PathVariable UUID id) {
        return service.findById(id);
    }

    @GetMapping
    public List<AssociationResponse> findAll() {
        return service.findAll();
    }
}