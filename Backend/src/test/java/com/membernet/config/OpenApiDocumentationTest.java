package com.membernet.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OpenApiDocumentationTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void openApiDocumentationIsAvailable() throws Exception {
        mvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.openapi").exists())
                .andExpect(jsonPath("$.info.title")
                        .value("MemberNet Sprint 2 API"))
                .andExpect(jsonPath("$.info.version")
                        .value("2.0.0"))
                .andExpect(jsonPath("$.paths['/api/auth/login']")
                        .exists())
                .andExpect(jsonPath("$.paths['/api/associations']")
                        .exists())
                .andExpect(jsonPath("$.paths['/api/payments']")
                        .exists());
    }
}