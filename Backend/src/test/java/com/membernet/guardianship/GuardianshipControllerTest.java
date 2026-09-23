package com.membernet.guardianship;

import java.time.LocalDate;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.membernet.auth.SessionAuthenticationInterceptor;
import com.membernet.auth.SessionResponse;
import com.membernet
        .user.AccountStatus;
import com.membernet
        .user.EventViewPreference;
import com.membernet
        .user.SpringDataUserAccountRepository;
import com.membernet
        .user.UserAccountEntity;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class GuardianshipControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SpringDataUserAccountRepository users;

    @Autowired
    private GuardianshipService guardianshipService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void guardianshipRelationshipCanBeCreated()
            throws Exception {

        UserAccountEntity guardian = createUser("guardian");
        UserAccountEntity child = createUser("child");

        String request = createRequest(
                guardian.getId(),
                child.getId()
        );

        mvc.perform(post("/api/guardianships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.guardianUserAccountId")
                        .value(guardian.getId().toString()))
                .andExpect(jsonPath("$.childUserAccountId")
                        .value(child.getId().toString()))
                .andExpect(jsonPath("$.status")
                        .value("PENDING"));
    }

    @Test
    void userCannotBeOwnGuardian()
            throws Exception {

        UserAccountEntity user = createUser("same-user");

        String request = createRequest(
                user.getId(),
                user.getId()
        );

        mvc.perform(post("/api/guardianships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(
                                "A user account cannot be "
                                + "its own guardian."
                        ));
    }

    @Test
    void guardianCanReadRelationship()
            throws Exception {

        UserAccountEntity guardian = createUser("guardian-read");
        UserAccountEntity child = createUser("child-read");

        GuardianshipResponse relationship =
                createRelationship(guardian, child);

        mvc.perform(get(
                        "/api/guardianships/{id}",
                        relationship.id()
                )
                        .session(authenticatedSession(guardian)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id")
                        .value(relationship.id().toString()))
                .andExpect(jsonPath("$.guardianUserAccountId")
                        .value(guardian.getId().toString()));
    }

    @Test
    void childCanReadRelationship()
            throws Exception {

        UserAccountEntity guardian = createUser("guardian-child");
        UserAccountEntity child = createUser("child-participant");

        GuardianshipResponse relationship =
                createRelationship(guardian, child);

        mvc.perform(get(
                        "/api/guardianships/{id}",
                        relationship.id()
                )
                        .session(authenticatedSession(child)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.childUserAccountId")
                        .value(child.getId().toString()));
    }

    @Test
    void unrelatedUserCannotReadRelationship()
            throws Exception {

        UserAccountEntity guardian = createUser("guardian-private");
        UserAccountEntity child = createUser("child-private");
        UserAccountEntity unrelated = createUser("unrelated");

        GuardianshipResponse relationship =
                createRelationship(guardian, child);

        mvc.perform(get(
                        "/api/guardianships/{id}",
                        relationship.id()
                )
                        .session(authenticatedSession(unrelated)))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value(
                                "You cannot access this guardianship "
                                + "relationship."
                        ));
    }

    @Test
    void userCannotCreateRequestAsAnotherGuardian()
            throws Exception {

        UserAccountEntity guardian = createUser("real-guardian");
        UserAccountEntity child = createUser("request-child");
        UserAccountEntity unrelated = createUser("false-guardian");

        String request = createRequest(
                guardian.getId(),
                child.getId()
        );

        mvc.perform(post("/api/guardianships")
                        .session(authenticatedSession(unrelated))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message")
                        .value(
                                "You can create a guardianship "
                                + "request only as the guardian."
                        ));
    }

    private GuardianshipResponse createRelationship(
            UserAccountEntity guardian,
            UserAccountEntity child) {

        return guardianshipService.create(
                new CreateGuardianshipRequest(
                        guardian.getId(),
                        child.getId(),
                        LocalDate.now(),
                        null
                )
        );
    }

    private String createRequest(
            UUID guardianId,
            UUID childId) {

        return """
                {
                  "guardianUserAccountId": "%s",
                  "childUserAccountId": "%s",
                  "validFrom": "%s",
                  "validUntil": null
                }
                """.formatted(
                guardianId,
                childId,
                LocalDate.now()
        );
    }

    private MockHttpSession authenticatedSession(
            UserAccountEntity user) {

        MockHttpSession session = new MockHttpSession();

        SessionResponse authenticatedUser =
                new SessionResponse(
                        true,
                        user.getId(),
                        user.getLoginEmail(),
                        user.getFirstName()
                                + " "
                                + user.getLastName(),
                        user.getAccountStatus(),
                        "MemberNet home"
                );

        session.setAttribute(
                SessionAuthenticationInterceptor
                        .AUTHENTICATED_USER_ATTRIBUTE,
                authenticatedUser
        );

        return session;
    }

    private UserAccountEntity createUser(String prefix) {
        String uniqueEmail = prefix
                + "-"
                + UUID.randomUUID()
                + "@example.com";

        UserAccountEntity user = new UserAccountEntity(
                uniqueEmail,
                passwordEncoder.encode("test-password"),
                "Test",
                "User",
                uniqueEmail,
                null,
                AccountStatus.ACTIVE,
                "en",
                "Europe/Helsinki",
                EventViewPreference.LIST
        );

        return users.saveAndFlush(user);
    }
}