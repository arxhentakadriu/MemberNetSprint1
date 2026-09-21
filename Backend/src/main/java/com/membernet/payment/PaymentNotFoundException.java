package com.membernet.payment;

public class PaymentNotFoundException extends RuntimeException {

    public PaymentNotFoundException() {
        super("Payment obligation was not found.");
    }
}