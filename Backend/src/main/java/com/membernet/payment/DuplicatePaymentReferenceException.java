package com.membernet.payment;

public class DuplicatePaymentReferenceException
        extends RuntimeException {

    public DuplicatePaymentReferenceException() {
        super("The payment reference already exists.");
    }
}