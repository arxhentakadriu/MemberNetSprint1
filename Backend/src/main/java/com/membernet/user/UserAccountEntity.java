package com.membernet.user;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_accounts")
public class UserAccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "login_email", nullable = false, unique = true, length = 255)
    private String loginEmail;

    @Column(name = "contact_email", length = 255)
    private String contactEmail;

    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status", nullable = false, length = 30)
    private AccountStatus accountStatus;

    @Column(name = "language_code", nullable = false, length = 10)
    private String languageCode;

    @Column(name = "time_zone", nullable = false, length = 100)
    private String timeZone;

    @Enumerated(EnumType.STRING)
    @Column(name = "event_view_preference", nullable = false, length = 20)
    private EventViewPreference eventViewPreference;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected UserAccountEntity() {
    }

    public UserAccountEntity(
            String loginEmail,
            String contactEmail,
            String passwordHash,
            String firstName,
            String lastName,
            LocalDate dateOfBirth,
            AccountStatus accountStatus,
            String languageCode,
            String timeZone,
            EventViewPreference eventViewPreference) {

        this.loginEmail = normalizeEmail(loginEmail);
        this.contactEmail = normalizeNullableEmail(contactEmail);
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.accountStatus = accountStatus;
        this.languageCode = languageCode;
        this.timeZone = timeZone;
        this.eventViewPreference = eventViewPreference;
    }

    @PrePersist
    void beforeInsert() {
        Instant now = Instant.now();
        createdAt = now;
        updatedAt = now;
    }

    @PreUpdate
    void beforeUpdate() {
        updatedAt = Instant.now();
    }

    private static String normalizeEmail(String email) {
        return email.trim().toLowerCase();
    }

    private static String normalizeNullableEmail(String email) {
        if (email == null || email.isBlank()) {
            return null;
        }

        return normalizeEmail(email);
    }

    public UUID getId() {
        return id;
    }

    public String getLoginEmail() {
        return loginEmail;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public AccountStatus getAccountStatus() {
        return accountStatus;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public EventViewPreference getEventViewPreference() {
        return eventViewPreference;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}