package com.zituhoq.banking_transaction.core.valueObjects;

import java.util.UUID;

public record TransactionId(UUID id) {
    public static TransactionId newId(){
        return new TransactionId(UUID.randomUUID());
    }
}
