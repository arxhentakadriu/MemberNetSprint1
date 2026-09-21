package com.membernet.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.security.session-protection.enabled=true"
})
@AutoConfigureMockMvc
class SessionAuthenticationInterceptorTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void protectedEndpointRejectsUnauthenticatedUser()
            throws Exception {

        mvc.perform(get("/api/associations"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Authentication is required."));
    }

    @Test
    void openApiDocumentationRemainsPublic()
            throws Exception {

        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }
}