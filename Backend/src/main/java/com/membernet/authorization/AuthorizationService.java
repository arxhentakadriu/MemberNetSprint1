package com.membernet.authorization;

import java.util.List;
import java.util.Locale;

import com.membernet.association.AssociationRepository;
import com.membernet.membership.Membership;
import com.membernet.membership.MembershipRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorizationService {

    private final AuthorizationRepository authorization;
    private final AssociationRepository associations;
    private final MembershipRepository memberships;

    public AuthorizationService(
            AuthorizationRepository authorization,
            AssociationRepository associations,
            MembershipRepository memberships) {

        this.authorization = authorization;
        this.associations = associations;
        this.memberships = memberships;
    }

    @Transactional
    public RoleResponse createRole(CreateRoleRequest request) {
        if (associations.findById(request.associationId()).isEmpty()) {
            throw new AuthorizationNotFoundException(
                    "The selected association does not exist."
            );
        }

        String code = normalizeCode(request.code());

        if (authorization.roleCodeExists(
                request.associationId(),
                code)) {

            throw new AuthorizationConflictException(
                    "A role with this code already exists in the association."
            );
        }

        RoleEntity entity = new RoleEntity(
                request.associationId(),
                code,
                request.name().trim(),
                normalizeOptional(request.description())
        );

        return RoleResponse.from(authorization.saveRole(entity));
    }

    @Transactional
    public PermissionResponse createPermission(
            CreatePermissionRequest request) {

        String code = normalizeCode(request.code());

        if (authorization.permissionCodeExists(code)) {
            throw new AuthorizationConflictException(
                    "A permission with this code already exists."
            );
        }

        PermissionEntity entity = new PermissionEntity(
                code,
                request.name().trim(),
                normalizeOptional(request.description())
        );

        return PermissionResponse.from(
                authorization.savePermission(entity)
        );
    }

    @Transactional
    public AssignmentResponse assignRole(
            AssignRoleRequest request) {

        Membership membership = memberships
                .findById(request.membershipId())
                .orElseThrow(() -> new AuthorizationNotFoundException(
                        "The selected membership does not exist."
                ));

        Role role = authorization
                .findRoleById(request.roleId())
                .orElseThrow(() -> new AuthorizationNotFoundException(
                        "The selected role does not exist."
                ));

        if (!membership.associationId().equals(role.associationId())) {
            throw new AuthorizationValidationException(
                    "The membership and role must belong to the same association."
            );
        }

        if (authorization.membershipHasRole(
                request.membershipId(),
                request.roleId())) {

            throw new AuthorizationConflictException(
                    "This role is already assigned to the membership."
            );
        }

        MembershipRoleEntity assignment =
                new MembershipRoleEntity(
                        request.membershipId(),
                        request.roleId(),
                        membership.associationId()
                );

        authorization.assignRoleToMembership(assignment);

        return new AssignmentResponse(
                "Role assigned to membership successfully."
        );
    }

    @Transactional
    public AssignmentResponse assignPermission(
            AssignPermissionRequest request) {

        authorization.findRoleById(request.roleId())
                .orElseThrow(() -> new AuthorizationNotFoundException(
                        "The selected role does not exist."
                ));

        authorization.findPermissionById(request.permissionId())
                .orElseThrow(() -> new AuthorizationNotFoundException(
                        "The selected permission does not exist."
                ));

        if (authorization.roleHasPermission(
                request.roleId(),
                request.permissionId())) {

            throw new AuthorizationConflictException(
                    "This permission is already assigned to the role."
            );
        }

        RolePermissionEntity assignment =
                new RolePermissionEntity(
                        request.roleId(),
                        request.permissionId()
                );

        authorization.assignPermissionToRole(assignment);

        return new AssignmentResponse(
                "Permission assigned to role successfully."
        );
    }

    @Transactional(readOnly = true)
    public List<RoleResponse> findRolesByAssociation(
            java.util.UUID associationId) {

        return authorization.findRolesByAssociationId(associationId)
                .stream()
                .map(RoleResponse::from)
                .toList();
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase(Locale.ROOT);
    }

    private String normalizeOptional(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return value.trim();
    }
}