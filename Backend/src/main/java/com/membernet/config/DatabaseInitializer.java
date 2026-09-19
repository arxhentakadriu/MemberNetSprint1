package com.membernet.config;

import java.time.ZoneId;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

import com.membernet.user.AccountStatus;
import com.membernet.user.EventViewPreference;
import com.membernet.user.SpringDataUserAccountRepository;
import com.membernet.user.UserAccountEntity;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initializeUserAccounts(
            SpringDataUserAccountRepository repository,
            PasswordEncoder passwordEncoder,
            @Value("${app.bootstrap.admin.email:}") String adminEmail,
            @Value("${app.bootstrap.admin.password:}") String adminPassword,
            @Value("${app.bootstrap.admin.first-name:Admin}") String firstName,
            @Value("${app.bootstrap.admin.last-name:User}") String lastName) {

        return args -> {
            if (!StringUtils.hasText(adminEmail)
                    || !StringUtils.hasText(adminPassword)) {

                System.out.println(
                        "Bootstrap admin was not created. "
                        + "ADMIN_EMAIL and ADMIN_PASSWORD are not configured."
                );

                return;
            }

            String normalizedEmail = adminEmail.trim().toLowerCase();

            if (repository.existsByLoginEmailIgnoreCase(normalizedEmail)) {
                return;
            }

            UserAccountEntity admin = new UserAccountEntity(
                    normalizedEmail,
                    normalizedEmail,
                    passwordEncoder.encode(adminPassword),
                    firstName,
                    lastName,
                    null,
                    AccountStatus.ACTIVE,
                    "en",
                    ZoneId.systemDefault().getId(),
                    EventViewPreference.LIST
            );

            repository.save(admin);

            System.out.println(
                    "Bootstrap administrator account created: "
                    + normalizedEmail
            );
        };
    }
}