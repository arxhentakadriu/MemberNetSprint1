# Development Log

## Step 1 – Requirements Analysis

The functional, technical, documentation and quality
requirements were reviewed.

## Step 2 – Technology Selection

Java 21, Spring Boot, Maven, HTML, CSS, JavaScript, PostgreSQL
and Docker were selected.

## Step 3 – Initial Authentication Prototype

An in-memory repository was initially used with demonstration
accounts to verify the authentication workflow.

## Step 4 – PostgreSQL Integration

The `membernet` database was created in PostgreSQL 18.
Spring Data JPA and the PostgreSQL driver were added.

## Step 5 – Persistent Accounts

The in-memory repository was removed. User accounts and roles
were stored in PostgreSQL.

## Step 6 – Password Security

Plain-text password comparison was replaced with BCrypt password
hashing.

## Step 7 – Role-Based Home Page

MEMBER users receive the Member home page. ADMIN users receive
the Administrator dashboard.

## Step 8 – User Interface Improvements

The interface text was changed from “Sign in” to “Login”.
Training account information was removed from the visible page.
Member IDs and display names were updated.

## Step 9 – Testing

Successful login, invalid credentials, account loading,
permissions and logout were tested.

## Step 10 – Documentation

Architecture, decisions, risks, testing and AI usage were
documented.
