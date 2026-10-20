package com.zituhoq.banking_transaction.in.web.dto;

import com.zituhoq.banking_transaction.core.domain.TransactionStatus;
import com.zituhoq.banking_transaction.core.domain.TransactionType;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record TransactionResponse (
        @NotNull UUID transactionId,
        @NotNull UUID sourceAccountId,
        @NotNull UUID targetAccountId,
        @NotNull BigDecimal amount,
        @NotNull Currency currency,
        @NotNull TransactionType type,
        @NotNull TransactionStatus status,
        @NotNull Instant createdAt
){}
