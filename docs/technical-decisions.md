# Technical Decisions

## Java and Spring Boot

Java was required by the project. Spring Boot was selected
because it supports REST APIs, layered architecture, validation,
monitoring and database integration.

## Maven

Maven was selected to manage dependencies, compile the
application, execute tests and create the application JAR.

## PostgreSQL

PostgreSQL was selected for persistent account storage. The
initial in-memory repository was replaced with a PostgreSQL
implementation.

## BCrypt

Passwords are not stored as plain text. BCrypt hashes are stored
in PostgreSQL and verified during authentication.

## HTML, CSS and JavaScript

A simple frontend was selected because Sprint 1 contains a small
authentication workflow and does not require a frontend
framework.

## Docker

Docker supports repeatable and container-based deployment.

## Kubernetes

Kubernetes was evaluated but postponed until Sprint 2, following
the project schedule.
