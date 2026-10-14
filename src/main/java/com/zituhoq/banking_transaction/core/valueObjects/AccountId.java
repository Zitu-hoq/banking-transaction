package com.zituhoq.banking_transaction.core.valueObjects;

import java.util.UUID;

public record AccountId(UUID id) {
    public static AccountId newId(){
        return new AccountId(UUID.randomUUID());
    }
}
