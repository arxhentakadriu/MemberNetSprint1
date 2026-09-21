package com.membernet.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionAuthenticationInterceptor
        implements HandlerInterceptor {

    private static final String AUTHENTICATED_USER_SESSION_KEY =
            "authenticatedUser";

    private final boolean sessionProtectionEnabled;

    public SessionAuthenticationInterceptor(
            @Value("${app.security.session-protection.enabled:true}")
            boolean sessionProtectionEnabled) {

        this.sessionProtectionEnabled =
                sessionProtectionEnabled;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) {

        if (!sessionProtectionEnabled) {
            return true;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new AuthenticationRequiredException();
        }

        Object authenticatedUser = session.getAttribute(
                AUTHENTICATED_USER_SESSION_KEY
        );

        if (!(authenticatedUser instanceof SessionResponse)) {
            throw new AuthenticationRequiredException();
        }

        return true;
    }
}