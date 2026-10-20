package com.zituhoq.banking_transaction.in.web.dto;

import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record WithdrawRequest(
        @NotNull AccountId accountId,
        @DecimalMin("1.00") BigDecimal amount,
        @NotNull Currency currency,
        @NotBlank String idempotencyKey,
        @NotBlank String correlationId
) {}