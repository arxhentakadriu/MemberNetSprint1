package com.membernet.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java
        .util.UUID;

import org.junit.jupiter
        .api.Test;
import static org.mockito
        .Mockito.doThrow;
import static org.mockito
        .Mockito.verify;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.crypto.password
        .PasswordEncoder;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers
        .jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.membernet.association.AssociationEntity;
import com.membernet.association.AssociationStatus;
import com.membernet.association.SpringDataAssociationRepository;
import com.membernet.auth.SessionAuthenticationInterceptor;
import com.membernet.auth.SessionResponse;
import com.membernet.authorization.AuthorizationForbiddenException;
import com.membernet.authorization.AuthorizationService;
import com.membernet.membership.MembershipEntity;
import com.membernet.membership.MembershipStatus;
import com.membernet.membership.SpringDataMembershipRepository;
import com.membernet.user.AccountStatus;
import com.membernet.user.EventViewPreference;
import com.membernet.user.SpringDataUserAccountRepository;
import com.membernet.user.UserAccountEntity;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
class PaymentControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthorizationService authorization;

    @Autowired
    private SpringDataUserAccountRepository users;

    @Autowired
    private SpringDataAssociationRepository associations;

    @Autowired
    private SpringDataMembershipRepository memberships;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void paymentObligationCanBeCreated() throws Exception {
        TestData data = createTestData();

        String reference = "PAY-" + UUID.randomUUID();

        String request = """
                {
                  "userAccountId": "%s",
                  "associationId": "%s",
                  "membershipId": "%s",
                  "amount": 49.90,
                  "currencyCode": "EUR",
                  "paymentReference": "%s",
                  "description": "Annual membership fee",
                  "dueDate": "%s"
                }
                """.formatted(
                data.user().getId(),
                data.association().getId(),
                data.membership().getId(),
                reference,
                LocalDate.now().plusDays(30)
        );

        mvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentReference")
                        .value(reference))
                .andExpect(jsonPath("$.amount")
                        .value(49.90))
                .andExpect(jsonPath("$.currencyCode")
                        .value("EUR"))
                .andExpect(jsonPath("$.status")
                        .value("OPEN"));
    }

    @Test
    void paymentStatusCanBeChangedToPaid()
            throws Exception {

        TestData data = createTestData();

        PaymentResponse payment = paymentService.create(
                new CreatePaymentRequest(
                        data.user().getId(),
                        data.association().getId(),
                        data.membership().getId(),
                        new BigDecimal("25.00"),
                        "EUR",
                        "PAY-" + UUID.randomUUID(),
                        "Training fee",
                        LocalDate.now().plusDays(10)
                )
        );

        String request = """
                {
                  "status": "PAID"
                }
                """;

        mvc.perform(patch(
                        "/api/payments/{id}/status",
                        payment.id()
                )
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status")
                        .value("PAID"))
                .andExpect(jsonPath("$.paidAt")
                        .isNotEmpty());
    }

    @Test
    void paymentAmountMustBePositive()
            throws Exception {

        TestData data = createTestData();

        String request = """
                {
                  "userAccountId": "%s",
                  "associationId": "%s",
                  "membershipId": "%s",
                  "amount": 0,
                  "currencyCode": "EUR",
                  "paymentReference": "PAY-%s",
                  "description": "Invalid payment",
                  "dueDate": "%s"
                }
                """.formatted(
                data.user().getId(),
                data.association().getId(),
                data.membership().getId(),
                UUID.randomUUID(),
                LocalDate.now().plusDays(10)
        );

        mvc.perform(post("/api/payments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message")
                        .value(
                                "Payment amount must be "
                                + "greater than zero."
                        ));
    }

    @Test
    void userWithPaymentPermissionCanCreatePayment()
            throws Exception {

        TestData data = createTestData();

        MockHttpSession session = authenticatedSession(
                data.user().getId()
        );

        String reference =
                "AUTHORIZED-" + UUID.randomUUID();

        String request = """
                {
                  "userAccountId": "%s",
                  "associationId": "%s",
                  "membershipId": "%s",
                  "amount": 35.00,
                  "currencyCode": "EUR",
                  "paymentReference": "%s",
                  "description": "Authorized payment test",
                  "dueDate": "%s"
                }
                """.formatted(
                data.user().getId(),
                data.association().getId(),
                data.membership().getId(),
                reference,
                LocalDate.now().plusDays(30)
        );

        mvc.perform(post("/api/payments")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.paymentReference")
                        .value(reference));

        verify(authorization).requirePermission(
                data.user().getId(),
                data.association().getId(),
                "PAYMENT_MANAGE"
        );
    }

    @Test
    void userWithoutPaymentPermissionReceivesForbidden()
            throws Exception {

        TestData data = createTestData();

        MockHttpSession session = authenticatedSession(
                data.user().getId()
        );

        String reference =
                "FORBIDDEN-" + UUID.randomUUID();

        doThrow(new AuthorizationForbiddenException(
                "You do not have the required permission: "
                        + "PAYMENT_MANAGE."
        )).when(authorization).requirePermission(
                data.user().getId(),
                data.association().getId(),
                "PAYMENT_MANAGE"
        );

        String request = """
                {
                  "userAccountId": "%s",
                  "associationId": "%s",
                  "membershipId": "%s",
                  "amount": 35.00,
                  "currencyCode": "EUR",
                  "paymentReference": "%s",
                  "description": "Forbidden payment test",
                  "dueDate": "%s"
                }
                """.formatted(
                data.user().getId(),
                data.association().getId(),
                data.membership().getId(),
                reference,
                LocalDate.now().plusDays(30)
        );

        mvc.perform(post("/api/payments")
                        .session(session)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(
                        "You do not have the required permission: "
                                + "PAYMENT_MANAGE."
                ));

        verify(authorization).requirePermission(
                data.user().getId(),
                data.association().getId(),
                "PAYMENT_MANAGE"
        );
    }

    private MockHttpSession authenticatedSession(
            UUID userAccountId) {

        MockHttpSession session = new MockHttpSession();

        session.setAttribute(
                SessionAuthenticationInterceptor
                        .AUTHENTICATED_USER_ATTRIBUTE,
                new SessionResponse(
                        true,
                        userAccountId,
                        "payment-user@example.com",
                        "Payment User",
                        AccountStatus.ACTIVE,
                        "MemberNet home"
                )
        );

        return session;
    }

    private TestData createTestData() {
        String uniqueValue = UUID.randomUUID().toString();

        UserAccountEntity user = users.saveAndFlush(
                new UserAccountEntity(
                        "payment-" + uniqueValue
                                + "@example.com",
                        passwordEncoder.encode(
                                "test-password"
                        ),
                        "Payment",
                        "User",
                        "payment-" + uniqueValue
                                + "@example.com",
                        null,
                        AccountStatus.ACTIVE,
                        "en",
                        "Europe/Helsinki",
                        EventViewPreference.LIST
                )
        );

        AssociationEntity association =
                associations.saveAndFlush(
                        new AssociationEntity(
                                "Payment Association "
                                        + uniqueValue,
                                "PAY-" + uniqueValue
                                        .substring(0, 8),
                                "BUS-" + uniqueValue,
                                "XK",
                                "association-" + uniqueValue
                                        + "@example.com",
                                AssociationStatus.ACTIVE,
                                true
                        )
                );

        MembershipEntity membership =
                memberships.saveAndFlush(
                        new MembershipEntity(
                                user.getId(),
                                association.getId(),
                                MembershipStatus.ACTIVE,
                                LocalDate.now(),
                                null
                        )
                );

        return new TestData(
                user,
                association,
                membership
        );
    }

    private record TestData(
            UserAccountEntity user,
            AssociationEntity association,
            MembershipEntity membership) {
    }
}