package com.membernet.association;

import java.util.Locale;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class AssociationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void validAssociationCanBeCreated() throws Exception {
        String uniqueValue = UUID.randomUUID().toString();
        String shortName = "TEST-" + uniqueValue.substring(0, 8);
        String businessId = "BUS-" + uniqueValue;

        String request = """
                {
                  "name": "Test Association",
                  "shortName": "%s",
                  "businessId": "%s",
                  "countryCode": "XK",
                  "email": "association-%s@example.com",
                  "termsAccepted": true
                }
                """.formatted(
                shortName,
                businessId,
                uniqueValue
        );

        mvc.perform(post("/api/associations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name")
                        .value("Test Association"))
                .andExpect(jsonPath("$.shortName")
                        .value(shortName.toUpperCase(Locale.ROOT)))
                .andExpect(jsonPath("$.businessId")
                        .value(businessId))
                .andExpect(jsonPath("$.countryCode")
                        .value("XK"))
                .andExpect(jsonPath("$.status")
                        .value("ACTIVE"))
                .andExpect(jsonPath("$.termsAccepted")
                        .value(true));
    }

    @Test
    void termsMustBeAccepted() throws Exception {
        String uniqueValue = UUID.randomUUID().toString();

        String request = """
                {
                  "name": "Invalid Association",
                  "shortName": "INVALID-%s",
                  "countryCode": "XK",
                  "email": "invalid-%s@example.com",
                  "termsAccepted": false
                }
                """.formatted(
                uniqueValue.substring(0, 8),
                uniqueValue
        );

        mvc.perform(post("/api/associations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Terms must be accepted."));
    }
}