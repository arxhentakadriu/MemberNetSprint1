package com.membernet.authorization;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito
        .Mockito.never;
import static org.mockito
        .Mockito.verify;
import org.springframework.beans.factory
        .annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web
        .MockHttpSession;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers
        .jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.membernet.auth.SessionAuthenticationInterceptor;
import com.membernet.auth.SessionResponse;
import com.membernet.user.AccountStatus;

@SpringBootTest
@AutoConfigureMockMvc
class AuthorizationPermissionControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorizationService authorizationService;

    @Test
    void userWithoutAuthorizationPermissionCannotCreateRole()
            throws Exception {

        UUID userAccountId = UUID.randomUUID();
        UUID associationId = UUID.randomUUID();

        doThrow(new AuthorizationForbiddenException(
                "You do not have the required permission: "
                        + "AUTHORIZATION_MANAGE."
        )).when(authorizationService).requirePermission(
                userAccountId,
                associationId,
                "AUTHORIZATION_MANAGE"
        );

        MockHttpSession session = new MockHttpSession();

        session.setAttribute(
                SessionAuthenticationInterceptor
                        .AUTHENTICATED_USER_ATTRIBUTE,
                new SessionResponse(
                        true,
                        userAccountId,
                        "unauthorized@example.com",
                        "Unauthorized User",
                        AccountStatus.ACTIVE,
                        "MemberNet home"
                )
        );

        String request = """
                {
                  "associationId": "%s",
                  "code": "FORBIDDEN_ROLE",
                  "name": "Forbidden Role",
                  "description": "Must not be created."
                }
                """.formatted(associationId);

        mvc.perform(post("/api/authorization/roles")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(
                        "You do not have the required permission: "
                                + "AUTHORIZATION_MANAGE."
                ));

        verify(authorizationService).requirePermission(
                userAccountId,
                associationId,
                "AUTHORIZATION_MANAGE"
        );

        verify(authorizationService, never())
                .createRole(any(CreateRoleRequest.class));
    }
}