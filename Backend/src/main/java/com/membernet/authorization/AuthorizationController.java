package com.membernet.authorization;

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
@RequestMapping("/api/authorization")
public class AuthorizationController {

    private final AuthorizationService service;

    public AuthorizationController(AuthorizationService service) {
        this.service = service;
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleResponse> createRole(
            @Valid @RequestBody CreateRoleRequest request) {

        RoleResponse response = service.createRole(request);

        return ResponseEntity
                .created(URI.create(
                        "/api/authorization/roles/" + response.id()
                ))
                .body(response);
    }

    @GetMapping("/roles/association/{associationId}")
    public List<RoleResponse> findRolesByAssociation(
            @PathVariable UUID associationId) {

        return service.findRolesByAssociation(associationId);
    }

    @PostMapping("/permissions")
    public ResponseEntity<PermissionResponse> createPermission(
            @Valid @RequestBody CreatePermissionRequest request) {

        PermissionResponse response =
                service.createPermission(request);

        return ResponseEntity
                .created(URI.create(
                        "/api/authorization/permissions/" + response.id()
                ))
                .body(response);
    }

    @PostMapping("/membership-roles")
    public AssignmentResponse assignRole(
            @Valid @RequestBody AssignRoleRequest request) {

        return service.assignRole(request);
    }

    @PostMapping("/role-permissions")
    public AssignmentResponse assignPermission(
            @Valid @RequestBody AssignPermissionRequest request) {

        return service.assignPermission(request);
    }
}