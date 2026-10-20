package com.zituhoq.banking_transaction.in.web.exception;

import java.time.Instant;

public record ErrorResponse(
        String code,
        String message,
        String correlationId,
        Instant timestamp
) {}