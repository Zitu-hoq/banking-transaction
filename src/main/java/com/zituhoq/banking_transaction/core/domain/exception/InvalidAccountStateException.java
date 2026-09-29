package com.zituhoq.banking_transaction.core.domain.exception;

public class InvalidAccountStateException extends RuntimeException {
    public InvalidAccountStateException() {
        super("Invalid Account State");
    }
}
