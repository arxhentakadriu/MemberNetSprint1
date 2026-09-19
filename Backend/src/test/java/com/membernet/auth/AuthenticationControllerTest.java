package com.membernet.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(properties = {
        "app.bootstrap.admin.email=test.admin@membernet.local",
        "app.bootstrap.admin.password=TestPassword123!",
        "app.bootstrap.admin.first-name=Test",
        "app.bootstrap.admin.last-name=Administrator"
})
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void activeUserCanLogInWithEmail() throws Exception {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "loginEmail": "test.admin@membernet.local",
                                  "password": "TestPassword123!"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Login successful. Welcome, Test Administrator."))
                .andExpect(jsonPath("$.loginEmail")
                        .value("test.admin@membernet.local"))
                .andExpect(jsonPath("$.displayName")
                        .value("Test Administrator"))
                .andExpect(jsonPath("$.accountStatus")
                        .value("ACTIVE"))
                .andExpect(jsonPath("$.homePage")
                        .value("MemberNet home"));
    }

    @Test
    void invalidCredentialsAreRejected() throws Exception {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "loginEmail": "test.admin@membernet.local",
                                  "password": "WrongPassword"
                                }
                                """))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("The username or password is incorrect."));
    }

    @Test
    void invalidEmailIsRejected() throws Exception {
        mvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "loginEmail": "not-an-email",
                                  "password": "TestPassword123!"
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value("Login email must be valid."));
    }
}