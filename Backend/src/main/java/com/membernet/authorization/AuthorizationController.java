package com.membernet.authorization;

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

import com.membernet.auth.SessionAuthenticationInterceptor;
import com.membernet.auth.SessionResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/authorization")
public class AuthorizationController {

    private static final String AUTHORIZATION_MANAGE =
            "AUTHORIZATION_MANAGE";

    private final AuthorizationService service;

    public AuthorizationController(
            AuthorizationService service) {

        this.service = service;
    }

    @PostMapping("/roles")
    public ResponseEntity<RoleResponse> createRole(
            @Valid @RequestBody CreateRoleRequest request,
            HttpServletRequest servletRequest) {

        UUID currentUserId =
                currentUserId(servletRequest);

        if (currentUserId != null) {
            service.requirePermission(
                    currentUserId,
                    request.associationId(),
                    AUTHORIZATION_MANAGE
            );
        }

        RoleResponse response =
                service.createRole(request);

        return ResponseEntity
                .created(URI.create(
                        "/api/authorization/roles/"
                                + response.id()
                ))
                .body(response);
    }

    @GetMapping("/roles/association/{associationId}")
    public List<RoleResponse> findRolesByAssociation(
            @PathVariable UUID associationId) {

        return service.findRolesByAssociation(
                associationId
        );
    }

    @PostMapping("/permissions")
    public ResponseEntity<PermissionResponse>
            createPermission(
                    @Valid @RequestBody
                    CreatePermissionRequest request,
                    HttpServletRequest servletRequest) {

        UUID currentUserId =
                currentUserId(servletRequest);

        if (currentUserId != null) {
            service.requirePermissionInAnyAssociation(
                    currentUserId,
                    AUTHORIZATION_MANAGE
            );
        }

        PermissionResponse response =
                service.createPermission(request);

        return ResponseEntity
                .created(URI.create(
                        "/api/authorization/permissions/"
                                + response.id()
                ))
                .body(response);
    }

    @PostMapping("/membership-roles")
    public AssignmentResponse assignRole(
            @Valid @RequestBody AssignRoleRequest request,
            HttpServletRequest servletRequest) {

        UUID currentUserId =
                currentUserId(servletRequest);

        if (currentUserId != null) {
            service.requirePermissionForMembership(
                    currentUserId,
                    request.membershipId(),
                    AUTHORIZATION_MANAGE
            );
        }

        return service.assignRole(request);
    }

    @PostMapping("/role-permissions")
    public AssignmentResponse assignPermission(
            @Valid @RequestBody
            AssignPermissionRequest request,
            HttpServletRequest servletRequest) {

        UUID currentUserId =
                currentUserId(servletRequest);

        if (currentUserId != null) {
            service.requirePermissionForRole(
                    currentUserId,
                    request.roleId(),
                    AUTHORIZATION_MANAGE
            );
        }

        return service.assignPermission(request);
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