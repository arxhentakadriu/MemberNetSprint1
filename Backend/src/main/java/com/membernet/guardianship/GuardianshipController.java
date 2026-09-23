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
import org.springframework.web.servlet.support
        .ServletUriComponentsBuilder;

import com.membernet.auth.SessionAuthenticationInterceptor;
import com.membernet.auth.SessionResponse;
import com.membernet.authorization.AuthorizationForbiddenException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
            @Valid @RequestBody
            CreateGuardianshipRequest request,
            HttpServletRequest servletRequest) {

        UUID currentUserId =
                currentUserId(servletRequest);

        if (currentUserId != null
                && !currentUserId.equals(
                        request.guardianUserAccountId()
                )) {

            throw new AuthorizationForbiddenException(
                    "You can create a guardianship "
                            + "request only as the guardian."
            );
        }

        GuardianshipResponse response =
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
    public GuardianshipResponse findById(
            @PathVariable UUID id,
            HttpServletRequest servletRequest) {

        GuardianshipResponse relationship =
                service.findById(id);

        authorizeParticipant(
                currentUserId(servletRequest),
                relationship
        );

        return relationship;
    }

    @GetMapping("/guardian/{guardianId}")
    public List<GuardianshipResponse> findByGuardian(
            @PathVariable UUID guardianId,
            HttpServletRequest servletRequest) {

        authorizeRequestedUser(
                currentUserId(servletRequest),
                guardianId
        );

        return service.findByGuardian(guardianId);
    }

    @GetMapping("/child/{childId}")
    public List<GuardianshipResponse> findByChild(
            @PathVariable UUID childId,
            HttpServletRequest servletRequest) {

        authorizeRequestedUser(
                currentUserId(servletRequest),
                childId
        );

        return service.findByChild(childId);
    }

    @PatchMapping("/{id}/status")
    public GuardianshipResponse changeStatus(
            @PathVariable UUID id,
            @Valid @RequestBody
            UpdateGuardianshipStatusRequest request,
            HttpServletRequest servletRequest) {

        GuardianshipResponse relationship =
                service.findById(id);

        authorizeParticipant(
                currentUserId(servletRequest),
                relationship
        );

        return service.changeStatus(
                id,
                request.status()
        );
    }

    private void authorizeParticipant(
            UUID currentUserId,
            GuardianshipResponse relationship) {

        if (currentUserId == null) {
            return;
        }

        boolean isGuardian = currentUserId.equals(
                relationship.guardianUserAccountId()
        );

        boolean isChild = currentUserId.equals(
                relationship.childUserAccountId()
        );

        if (!isGuardian && !isChild) {
            throw new AuthorizationForbiddenException(
                    "You cannot access this guardianship "
                            + "relationship."
            );
        }
    }

    private void authorizeRequestedUser(
            UUID currentUserId,
            UUID requestedUserId) {

        if (currentUserId == null
                || currentUserId.equals(requestedUserId)) {

            return;
        }

        throw new AuthorizationForbiddenException(
                "You cannot access guardianship "
                        + "relationships for this user."
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