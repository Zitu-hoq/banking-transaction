package com.zituhoq.banking_transaction.core.domain.exception;

public class NegativeAmountException extends RuntimeException {
    public NegativeAmountException() {
        super("Negative amount is invalid");
    }
}
