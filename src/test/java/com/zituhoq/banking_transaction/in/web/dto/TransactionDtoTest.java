package com.zituhoq.banking_transaction.in.web.dto;

import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TransactionDtoTest {

    // validator variable for validation
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    void shouldHoldValuesTest(){
        AccountId id = AccountId.newId();
        BigDecimal amount = new BigDecimal("1.00");
        Currency currency = Currency.USD;
        String idempotencyKey = ("1");
        String correlationId = ("x-11");

        DepositRequest req = new DepositRequest(id, amount, currency, idempotencyKey, correlationId);

        assertEquals(id, req.accountId());
        assertEquals(amount, req.amount());
        assertEquals(currency, req.currency());
        assertEquals(idempotencyKey, req.idempotencyKey());
        assertEquals(correlationId, req.correlationId());
    }


    @Test
    void shouldShowValidationMessage() {
        DepositRequest req = new DepositRequest(
                AccountId.newId(),
                new BigDecimal("0.50"), // invalid
                Currency.USD,
                "idem-001",
                "corr-001"
        );

        Set<ConstraintViolation<DepositRequest>> violations = validator.validate(req);

        assertFalse(violations.isEmpty());
        String msg = violations.iterator().next().getMessage();
        assertEquals("must be greater than or equal to 1.00", msg);
    }
}
