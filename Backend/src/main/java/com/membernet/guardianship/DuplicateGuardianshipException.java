package com.membernet.guardianship;

public class DuplicateGuardianshipException extends RuntimeException {

    public DuplicateGuardianshipException() {
        super(
                "A guardianship relationship already exists "
                + "between these user accounts."
        );
    }
}