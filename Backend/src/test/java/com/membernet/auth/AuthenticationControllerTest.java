package com.membernet.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                        .content(validLoginRequest()))
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
    void authenticatedUserCanReadCurrentSession() throws Exception {
        MvcResult loginResult = mvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validLoginRequest()))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession)
                loginResult.getRequest().getSession(false);

        mvc.perform(get("/api/auth/session")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.authenticated")
                        .value(true))
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
    void sessionEndpointRejectsUnauthenticatedRequest()
            throws Exception {

        mvc.perform(get("/api/auth/session"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message")
                        .value("Authentication is required."));
    }

    @Test
    void authenticatedUserCanLogOut() throws Exception {
        MvcResult loginResult = mvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(validLoginRequest()))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession)
                loginResult.getRequest().getSession(false);

        mvc.perform(post("/api/auth/logout")
                        .session(session))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Logout successful."));
    }

    @Test
    void logoutWithoutSessionIsAlsoSuccessful()
            throws Exception {

        mvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message")
                        .value("Logout successful."));
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

    private String validLoginRequest() {
        return """
                {
                  "loginEmail": "test.admin@membernet.local",
                  "password": "TestPassword123!"
                }
                """;
    }
}