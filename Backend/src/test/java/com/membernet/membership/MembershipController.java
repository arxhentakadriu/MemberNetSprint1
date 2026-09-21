package com.membernet.membership;

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
import com.membernet.user.AccountStatus;
import com.membernet.user.EventViewPreference;
import com.membernet.user.SpringDataUserAccountRepository;
import com.membernet.user.UserAccountEntity;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class MembershipControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private SpringDataUserAccountRepository users;

    @Autowired
    private SpringDataAssociationRepository associations;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UUID userAccountId;
    private UUID associationId;

    @BeforeEach
    void prepareData() {
        String uniqueValue = UUID.randomUUID().toString();

        UserAccountEntity user = new UserAccountEntity(
                "member-" + uniqueValue + "@example.com",
                passwordEncoder.encode("TestPassword123!"),
                "Test",
                "Member",
                null,
                null,
                AccountStatus.ACTIVE,
                "en",
                "Europe/Pristina",
                EventViewPreference.LIST
        );

        userAccountId = users.save(user).getId();

        AssociationEntity association = new AssociationEntity(
                "Test Association",
                "TEST-" + uniqueValue.substring(0, 8),
                "BUS-" + uniqueValue,
                "XK",
                "association-" + uniqueValue + "@example.com",
                AssociationStatus.ACTIVE,
                true
        );

        associationId = associations.save(association).getId();
    }

    @Test
    void validMembershipCanBeCreated() throws Exception {
        String request = """
                {
                  "userAccountId": "%s",
                  "associationId": "%s",
                  "validFrom": "%s",
                  "validUntil": null
                }
                """.formatted(
                userAccountId,
                associationId,
                LocalDate.now()
        );

        mvc.perform(post("/api/memberships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.userAccountId")
                        .value(userAccountId.toString()))
                .andExpect(jsonPath("$.associationId")
                        .value(associationId.toString()))
                .andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void validUntilCannotBeBeforeValidFrom() throws Exception {
        LocalDate validFrom = LocalDate.now();
        LocalDate validUntil = validFrom.minusDays(1);

        String request = """
                {
                  "userAccountId": "%s",
                  "associationId": "%s",
                  "validFrom": "%s",
                  "validUntil": "%s"
                }
                """.formatted(
                userAccountId,
                associationId,
                validFrom,
                validUntil
        );

        mvc.perform(post("/api/memberships")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Valid-until date cannot be before valid-from date."));
    }
}