package com.membernet.authorization;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.membernet.association.AssociationEntity;
import com.membernet.association.AssociationStatus;
import com.membernet.association.SpringDataAssociationRepository;
import com.membernet.membership.MembershipEntity;
import com.membernet.membership.MembershipStatus;
import com.membernet.membership.SpringDataMembershipRepository;
import com.membernet.user.AccountStatus;
import com.membernet.user.EventViewPreference;
import com.membernet.user.SpringDataUserAccountRepository;
import com.membernet.user.UserAccountEntity;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class AuthorizationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SpringDataUserAccountRepository users;

    @Autowired
    private SpringDataAssociationRepository associations;

    @Autowired
    private SpringDataMembershipRepository memberships;

    @Autowired
    private SpringDataRoleRepository roles;

    @Autowired
    private SpringDataPermissionRepository permissions;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UUID associationId;
    private UUID membershipId;

    @BeforeEach
    void prepareData() {
        String uniqueValue = UUID.randomUUID().toString();

        AssociationEntity association = new AssociationEntity(
                "Authorization Test Association",
                "AUTH-" + uniqueValue.substring(0, 8),
                "AUTH-BUS-" + uniqueValue,
                "XK",
                "authorization-" + uniqueValue + "@example.com",
                AssociationStatus.ACTIVE,
                true
        );

        associationId = associations.save(association).getId();

        UserAccountEntity user = new UserAccountEntity(
                "authorization-user-" + uniqueValue + "@example.com",
                passwordEncoder.encode("TestPassword123!"),
                "Authorization",
                "User",
                null,
                null,
                AccountStatus.ACTIVE,
                "en",
                "Europe/Pristina",
                EventViewPreference.LIST
        );

        UUID userAccountId = users.save(user).getId();

        MembershipEntity membership = new MembershipEntity(
                userAccountId,
                associationId,
                MembershipStatus.ACTIVE,
                LocalDate.now(),
                null
        );

        membershipId = memberships.save(membership).getId();
    }

    @Test
    void roleCanBeCreatedForAssociation() throws Exception {
        String code = "event-manager";

        String request = """
                {
                  "associationId": "%s",
                  "code": "%s",
                  "name": "Event Manager",
                  "description": "Can manage association events."
                }
                """.formatted(associationId, code);

        mvc.perform(post("/api/authorization/roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.associationId")
                        .value(associationId.toString()))
                .andExpect(jsonPath("$.code")
                        .value("EVENT-MANAGER"))
                .andExpect(jsonPath("$.name")
                        .value("Event Manager"));
    }

    @Test
    void permissionCanBeCreated() throws Exception {
        String uniqueCode = "EVENT_CREATE_"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8);

        String request = """
                {
                  "code": "%s",
                  "name": "Create Events",
                  "description": "Allows creation of events."
                }
                """.formatted(uniqueCode);

        mvc.perform(post("/api/authorization/permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code")
        .value(uniqueCode.toUpperCase(java.util.Locale.ROOT)))
                .andExpect(jsonPath("$.name")
                        .value("Create Events"));
    }

    @Test
    void roleCanBeAssignedToMembership() throws Exception {
        RoleEntity role = roles.save(new RoleEntity(
                associationId,
                "MEMBER_MANAGER",
                "Member Manager",
                "Can manage association members."
        ));

        String request = """
                {
                  "membershipId": "%s",
                  "roleId": "%s"
                }
                """.formatted(membershipId, role.getId());

        mvc.perform(post("/api/authorization/membership-roles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Role assigned to membership successfully."));
    }

    @Test
    void permissionCanBeAssignedToRole() throws Exception {
        RoleEntity role = roles.save(new RoleEntity(
                associationId,
                "ASSOCIATION_ADMIN",
                "Association Administrator",
                "Manages the association."
        ));

        String uniqueCode = "MEMBER_EDIT_"
                + UUID.randomUUID()
                .toString()
                .substring(0, 8);

        PermissionEntity permission =
                permissions.save(new PermissionEntity(
                        uniqueCode,
                        "Edit Members",
                        "Allows editing member information."
                ));

        String request = """
                {
                  "roleId": "%s",
                  "permissionId": "%s"
                }
                """.formatted(
                role.getId(),
                permission.getId()
        );

        mvc.perform(post("/api/authorization/role-permissions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Permission assigned to role successfully."));
    }
}