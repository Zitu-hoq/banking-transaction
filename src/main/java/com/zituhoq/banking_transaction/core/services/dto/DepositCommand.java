package com.zituhoq.banking_transaction.core.services.dto;

import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Money;

public record DepositCommand(
        AccountId accountId,
        Money amount,
        String idempotencyKey,
        String correlationId
) {}