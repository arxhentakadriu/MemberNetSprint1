package com.membernet.association;

import java.util.UUID;

public class AssociationNotFoundException extends RuntimeException {

    public AssociationNotFoundException(UUID id) {
        super("Association with ID " + id + " was not found.");
    }
}