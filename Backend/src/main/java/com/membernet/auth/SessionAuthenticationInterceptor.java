package com.membernet.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class SessionAuthenticationInterceptor
        implements HandlerInterceptor {

    public static final String AUTHENTICATED_USER_ATTRIBUTE =
            "authenticatedUser";

    private final boolean sessionProtectionEnabled;

    public SessionAuthenticationInterceptor(
            @Value(
                "${app.security.session-protection.enabled:true}"
            )
            boolean sessionProtectionEnabled) {

        this.sessionProtectionEnabled = sessionProtectionEnabled;
    }

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws IOException {

        if (!sessionProtectionEnabled) {
            return true;
        }

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        HttpSession session = request.getSession(false);

        if (session != null
                && session.getAttribute(
                        AUTHENTICATED_USER_ATTRIBUTE
                ) != null) {

            return true;
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(
                "{\"message\":\"Authentication is required.\"}"
        );

        return false;
    }
}