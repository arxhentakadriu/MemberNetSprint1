package com.membernet.membership;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

import org.junit.jupiter
        .api.Test;
import static org.mockito
        .Mockito.doThrow;
import static org.mockito
        .Mockito.verify;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.context.bean.override.mockito
        .MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers
        .jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.membernet.auth.SessionAuthenticationInterceptor;
import com.membernet.auth.SessionResponse;
import com.membernet.authorization.AuthorizationForbiddenException;
import com.membernet.authorization.AuthorizationService;
import com.membernet.user.AccountStatus;

@SpringBootTest
@AutoConfigureMockMvc
class MembershipAuthorizationControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private MembershipService membershipService;

    @MockitoBean
    private AuthorizationService authorizationService;

    @Test
    void userCannotReadAnotherUsersMembership()
            throws Exception {

        UUID membershipId = UUID.randomUUID();
        UUID ownerUserId = UUID.randomUUID();
        UUID currentUserId = UUID.randomUUID();
        UUID associationId = UUID.randomUUID();

        MembershipResponse membership =
                new MembershipResponse(
                        membershipId,
                        ownerUserId,
                        associationId,
                        MembershipStatus.ACTIVE,
                        LocalDate.now(),
                        null,
                        OffsetDateTime.now(),
                        OffsetDateTime.now()
                );

        when(membershipService.findById(membershipId))
                .thenReturn(membership);

        doThrow(new AuthorizationForbiddenException(
                "You do not have an active membership "
                        + "in the selected association."
        )).when(authorizationService).requirePermission(
                currentUserId,
                associationId,
                "MEMBERSHIP_MANAGE"
        );

        MockHttpSession session = new MockHttpSession();

        session.setAttribute(
                SessionAuthenticationInterceptor
                        .AUTHENTICATED_USER_ATTRIBUTE,
                new SessionResponse(
                        true,
                        currentUserId,
                        "other-user@example.com",
                        "Other User",
                        AccountStatus.ACTIVE,
                        "MemberNet home"
                )
        );

        mvc.perform(get(
                        "/api/memberships/{id}",
                        membershipId
                )
                        .session(session))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(
                        "You do not have an active membership "
                                + "in the selected association."
                ));

        verify(authorizationService).requirePermission(
                currentUserId,
                associationId,
                "MEMBERSHIP_MANAGE"
        );
    }
}