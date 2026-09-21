package com.membernet.guardianship;

import java.time.LocalDate;
import java.util.UUID;

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

import com.membernet.user.AccountStatus;
import com.membernet.user.EventViewPreference;
import com.membernet.user.SpringDataUserAccountRepository;
import com.membernet.user.UserAccountEntity;

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
    private PasswordEncoder passwordEncoder;

    @Test
    void guardianshipRelationshipCanBeCreated()
            throws Exception {

        UserAccountEntity guardian =
                createUser("guardian");

        UserAccountEntity child =
                createUser("child");

        String request = """
                {
                  "guardianUserAccountId": "%s",
                  "childUserAccountId": "%s",
                  "validFrom": "%s",
                  "validUntil": null
                }
                """.formatted(
                guardian.getId(),
                child.getId(),
                LocalDate.now()
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

        UserAccountEntity user =
                createUser("same-user");

        String request = """
                {
                  "guardianUserAccountId": "%s",
                  "childUserAccountId": "%s",
                  "validFrom": "%s",
                  "validUntil": null
                }
                """.formatted(
                user.getId(),
                user.getId(),
                LocalDate.now()
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