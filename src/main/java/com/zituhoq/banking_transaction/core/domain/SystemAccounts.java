package com.zituhoq.banking_transaction.core.domain;

import com.zituhoq.banking_transaction.core.valueObjects.AccountId;

public final class SystemAccounts {
    public static final AccountId SYSTEM     = new AccountId(0L);
    public static final AccountId CASH       = new AccountId(-1L);
    public static final AccountId FEES       = new AccountId(-2L);
    public static final AccountId INTEREST   = new AccountId(-3L);
    public static final AccountId TAX        = new AccountId(-4L);
    public static final AccountId REVERSAL   = new AccountId(-5L);
    public static final AccountId CHARGEBACK = new AccountId(-6L);
    public static final AccountId CLEARING   = new AccountId(-7L);
    public static final AccountId BONUS      = new AccountId(-8L);

    private SystemAccounts() {}
}
