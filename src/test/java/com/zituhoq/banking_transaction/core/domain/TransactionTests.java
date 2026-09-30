package com.zituhoq.banking_transaction.core.domain;

import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionTests {

    private Transaction transaction;
    private TransactionId transactionId;
    private AccountId sourceAccountId;
    private AccountId targetAccountId;
    private Money amount;
    private Instant createdAt;

    @BeforeEach
    void setUp() {
        transactionId = new TransactionId(1L);
        sourceAccountId = new AccountId(10L);
        targetAccountId = new AccountId(20L);
        amount = new Money(BigDecimal.valueOf(500), Currency.BDT);
        createdAt = Instant.now();
        transaction = new Transaction(
                transactionId,
                sourceAccountId,
                targetAccountId,
                amount,
                createdAt,
                TransactionType.TRANSFER,
                TransactionStatus.PENDING
        );
    }

    private Object getField(Object target, String fieldName) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(target);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to get field: " + fieldName, e);
        }
    }

    @Test
    void shouldMarkTransactionAsCompleted() {
        transaction.markAsCompleted();
        assertEquals(TransactionStatus.COMPLETED, getField(transaction, "status"));
    }

    @Test
    void shouldMarkTransactionAsFailed() {
        transaction.markAsFailed();
        assertEquals(TransactionStatus.FAILED, getField(transaction, "status"));
    }

    @Test
    void shouldPreserveTransactionId() {
        transaction.markAsCompleted();
        assertEquals(transactionId, getField(transaction, "transactionId"));
    }

    @Test
    void shouldPreserveSourceAccountId() {
        transaction.markAsCompleted();
        assertEquals(sourceAccountId, getField(transaction, "sourceAccountId"));
    }

    @Test
    void shouldPreserveTargetAccountId() {
        transaction.markAsCompleted();
        assertEquals(targetAccountId, getField(transaction, "targetAccountId"));
    }

    @Test
    void shouldPreserveAmount() {
        transaction.markAsCompleted();
        assertEquals(amount, getField(transaction, "amount"));
    }

    @Test
    void shouldPreserveCreatedAt() {
        transaction.markAsCompleted();
        assertEquals(createdAt, getField(transaction, "createdAt"));
    }

    @Test
    void shouldPreserveType() {
        transaction.markAsCompleted();
        assertEquals(TransactionType.TRANSFER, getField(transaction, "type"));
    }

    @Test
    void shouldAllowStatusTransitionFromCompletedToFailed() {
        transaction.markAsCompleted();
        assertEquals(TransactionStatus.COMPLETED, getField(transaction, "status"));
        transaction.markAsFailed();
        assertEquals(TransactionStatus.FAILED, getField(transaction, "status"));
    }

    @Test
    void shouldAllowStatusTransitionFromFailedToCompleted() {
        transaction.markAsFailed();
        assertEquals(TransactionStatus.FAILED, getField(transaction, "status"));
        transaction.markAsCompleted();
        assertEquals(TransactionStatus.COMPLETED, getField(transaction, "status"));
    }
}
