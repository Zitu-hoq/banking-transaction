package com.zituhoq.banking_transaction.core.domain;

import com.zituhoq.banking_transaction.core.valueObjects.AccountId;

import java.util.UUID;

public final class SystemAccounts {
    public static final AccountId SYSTEM =
            new AccountId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
    public static final AccountId CASH =
            new AccountId(UUID.fromString("22222222-2222-2222-2222-222222222222"));
    public static final AccountId FEES =
            new AccountId(UUID.fromString("33333333-3333-3333-3333-333333333333"));
    public static final AccountId INTEREST =
            new AccountId(UUID.fromString("44444444-4444-4444-4444-444444444444"));
    public static final AccountId TAX =
            new AccountId(UUID.fromString("55555555-5555-5555-5555-555555555555"));
    public static final AccountId REVERSAL =
            new AccountId(UUID.fromString("66666666-6666-6666-6666-666666666666"));
    public static final AccountId CHARGEBACK =
            new AccountId(UUID.fromString("77777777-7777-7777-7777-777777777777"));
    public static final AccountId CLEARING =
            new AccountId(UUID.fromString("88888888-8888-8888-8888-888888888888"));
    public static final AccountId BONUS =
            new AccountId(UUID.fromString("99999999-9999-9999-9999-999999999999"));

    private SystemAccounts() {}
}
