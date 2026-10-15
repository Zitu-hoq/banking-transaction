package com.zituhoq.banking_transaction.core.services.dto;

import com.zituhoq.banking_transaction.core.domain.TransactionStatus;
import com.zituhoq.banking_transaction.core.domain.TransactionType;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;

import java.time.Instant;

public record TransactionResult(
        TransactionId transactionId,
        AccountId sourceAccountId,
        AccountId targetAccountId,
        Money amount,
        TransactionType type,
        TransactionStatus status,
        Instant createdAt
) {}
