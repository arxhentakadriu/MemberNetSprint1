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

import com.membernet.auth.SessionAuthenticationInterceptor;
import com.membernet.auth.SessionResponse;
import com.membernet.authorization.AuthorizationForbiddenException;
import com.membernet.authorization.AuthorizationService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/memberships")
public class MembershipController {

    private static final String MEMBERSHIP_MANAGE =
            "MEMBERSHIP_MANAGE";

    private final MembershipService service;
    private final AuthorizationService authorization;

    public MembershipController(
            MembershipService service,
            AuthorizationService authorization) {

        this.service = service;
        this.authorization = authorization;
    }

    @PostMapping
    public ResponseEntity<MembershipResponse> create(
            @Valid @RequestBody CreateMembershipRequest request,
            HttpServletRequest servletRequest) {

        UUID currentUserId =
                currentUserId(servletRequest);

        if (currentUserId != null) {
            authorization.requirePermission(
                    currentUserId,
                    request.associationId(),
                    MEMBERSHIP_MANAGE
            );
        }

        MembershipResponse response =
                service.create(request);

        return ResponseEntity
                .created(URI.create(
                        "/api/memberships/"
                                + response.id()
                ))
                .body(response);
    }

    @GetMapping("/{id}")
    public MembershipResponse findById(
            @PathVariable UUID id,
            HttpServletRequest servletRequest) {

        MembershipResponse membership =
                service.findById(id);

        authorizeMembershipRead(
                currentUserId(servletRequest),
                membership
        );

        return membership;
    }

    @GetMapping("/user/{userAccountId}")
    public List<MembershipResponse> findByUser(
            @PathVariable UUID userAccountId,
            HttpServletRequest servletRequest) {

        List<MembershipResponse> memberships =
                service.findByUser(userAccountId);

        authorizeMembershipListRead(
                currentUserId(servletRequest),
                userAccountId,
                memberships
        );

        return memberships;
    }

    @GetMapping("/association/{associationId}")
    public List<MembershipResponse> findByAssociation(
            @PathVariable UUID associationId,
            HttpServletRequest servletRequest) {

        UUID currentUserId =
                currentUserId(servletRequest);

        if (currentUserId != null) {
            authorization.requirePermission(
                    currentUserId,
                    associationId,
                    MEMBERSHIP_MANAGE
            );
        }

        return service.findByAssociation(
                associationId
        );
    }

    private void authorizeMembershipRead(
            UUID currentUserId,
            MembershipResponse membership) {

        if (currentUserId == null
                || currentUserId.equals(
                        membership.userAccountId()
                )) {

            return;
        }

        authorization.requirePermission(
                currentUserId,
                membership.associationId(),
                MEMBERSHIP_MANAGE
        );
    }

    private void authorizeMembershipListRead(
            UUID currentUserId,
            UUID requestedUserId,
            List<MembershipResponse> memberships) {

        if (currentUserId == null
                || currentUserId.equals(
                        requestedUserId
                )) {

            return;
        }

        if (memberships.isEmpty()) {
            throw new AuthorizationForbiddenException(
                    "You cannot access memberships "
                            + "for this user."
            );
        }

        memberships.stream()
                .map(MembershipResponse::associationId)
                .distinct()
                .forEach(associationId ->
                        authorization.requirePermission(
                                currentUserId,
                                associationId,
                                MEMBERSHIP_MANAGE
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