package com.membernet.payment;

public class PaymentResourceNotFoundException
        extends RuntimeException {

    public PaymentResourceNotFoundException(String resource) {
        super(resource + " was not found.");
    }
}