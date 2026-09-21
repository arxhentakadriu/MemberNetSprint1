package com.membernet.guardianship;

public class GuardianshipNotFoundException extends RuntimeException {

    public GuardianshipNotFoundException() {
        super("Guardianship relationship was not found.");
    }
}