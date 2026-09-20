package com.membernet.membership;

import java.util.UUID;

public class MembershipNotFoundException extends RuntimeException {

    public MembershipNotFoundException(UUID id) {
        super("Membership with ID " + id + " was not found.");
    }
}