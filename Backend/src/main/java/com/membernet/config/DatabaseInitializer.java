package com.membernet.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.membernet.user.Role;
import com.membernet.user.SpringDataUserAccountRepository;
import com.membernet.user.UserAccountEntity;

@Configuration
public class DatabaseInitializer {

    @Bean
    CommandLineRunner initializeUsers(
            SpringDataUserAccountRepository repository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            createUserIfMissing(
                    repository,
                    passwordEncoder,
                    "member",
                    "member123",
                    "Member",
                    "1001",
                    Set.of(Role.MEMBER)
            );

            createUserIfMissing(
                    repository,
                    passwordEncoder,
                    "admin",
                    "admin123",
                    "Administrator",
                    "0001",
                    Set.of(Role.MEMBER, Role.ADMIN)
            );
            createUserIfMissing(
                    repository,
                    passwordEncoder,
                    "arxhenta",
                    "silver",
                    "Administrator",
                    "0002",
                    Set.of(Role.MEMBER, Role.ADMIN)
            );
        };
    }

    private void createUserIfMissing(
            SpringDataUserAccountRepository repository,
            PasswordEncoder passwordEncoder,
            String username,
            String password,
            String displayName,
            String memberId,
            Set<Role> roles) {

        if (!repository.existsByUsernameIgnoreCase(username)) {
            UserAccountEntity user = new UserAccountEntity(
                    username,
                    passwordEncoder.encode(password),
                    displayName,
                    memberId,
                    roles
            );

            repository.save(user);

            System.out.println(
                    "Database user created: " + username
            );
        }
    }
}