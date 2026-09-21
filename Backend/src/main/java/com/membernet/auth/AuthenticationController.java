package com.membernet.auth;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private static final String AUTHENTICATED_USER_SESSION_KEY =
            "authenticatedUser";

    private final AuthenticationService authenticationService;

    public AuthenticationController(
            AuthenticationService authenticationService) {

        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @Valid @RequestBody LoginRequest request,
            HttpServletRequest httpRequest) {

        LoginResponse response =
                authenticationService.authenticate(request);

        HttpSession previousSession =
                httpRequest.getSession(false);

        if (previousSession != null) {
            previousSession.invalidate();
        }

        HttpSession session = httpRequest.getSession(true);

        session.setAttribute(
                AUTHENTICATED_USER_SESSION_KEY,
                SessionResponse.from(response)
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/session")
    public ResponseEntity<SessionResponse> session(
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new AuthenticationRequiredException();
        }

        Object authenticatedUser = session.getAttribute(
                AUTHENTICATED_USER_SESSION_KEY
        );

        if (!(authenticatedUser
                instanceof SessionResponse sessionResponse)) {

            throw new AuthenticationRequiredException();
        }

        return ResponseEntity.ok(sessionResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(
            HttpServletRequest request) {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();
        }

        return ResponseEntity.ok(
                Map.of("message", "Logout successful.")
        );
    }
}