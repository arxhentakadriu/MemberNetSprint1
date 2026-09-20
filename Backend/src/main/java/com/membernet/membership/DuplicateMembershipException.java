package com.membernet.membership;

public class DuplicateMembershipException extends RuntimeException {

    public DuplicateMembershipException() {
        super("This user already has a membership in this association.");
    }
}