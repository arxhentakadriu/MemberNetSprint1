package com.membernet.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.membernet.auth.SessionAuthenticationInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final SessionAuthenticationInterceptor authenticationInterceptor;

    public WebConfig(
            SessionAuthenticationInterceptor authenticationInterceptor) {

        this.authenticationInterceptor = authenticationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        registry.addInterceptor(authenticationInterceptor)
                .addPathPatterns("/api/**")
                .excludePathPatterns(
                        "/api/auth/login",
                        "/api/auth/logout",
                        "/api/auth/session"
                );
    }
}