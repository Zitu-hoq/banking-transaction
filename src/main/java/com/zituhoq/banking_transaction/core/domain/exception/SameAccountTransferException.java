package com.zituhoq.banking_transaction.core.domain.exception;

public class SameAccountTransferException extends RuntimeException {
    public SameAccountTransferException() {
        super("Source and Target account can not be same!");
    }
}
