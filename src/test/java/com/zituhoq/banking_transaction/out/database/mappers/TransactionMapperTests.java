package com.zituhoq.banking_transaction.out.database.mappers;

import com.zituhoq.banking_transaction.core.domain.Transaction;
import com.zituhoq.banking_transaction.core.domain.TransactionStatus;
import com.zituhoq.banking_transaction.core.domain.TransactionType;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;
import com.zituhoq.banking_transaction.out.database.entities.TransactionJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionMapperTests {

    private TransactionMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new TransactionMapperImpl();
    }

    @Test
    void shouldMapDomainToJpaEntity() {
        TransactionId transactionId = TransactionId.newId();
        AccountId sourceAccountId = AccountId.newId();
        AccountId targetAccountId = AccountId.newId();
        Money amount = new Money(BigDecimal.valueOf(500), Currency.BDT);
        Instant createdAt = Instant.now();

        Transaction transaction = new Transaction(
                transactionId, sourceAccountId, targetAccountId, amount, createdAt,
                TransactionType.TRANSFER, TransactionStatus.PENDING
        );

        TransactionJpaEntity entity = mapper.toTransactionJpaEntity(transaction);

        assertNotNull(entity);
        assertEquals(sourceAccountId.id(), entity.getSourceAccountId());
        assertEquals(targetAccountId.id(), entity.getTargetAccountId());
        assertEquals(amount.amount(), entity.getAmount());
        assertEquals(amount.currency(), entity.getCurrency());
        assertEquals(TransactionType.TRANSFER, entity.getType());
        assertEquals(TransactionStatus.PENDING, entity.getStatus());
        assertEquals(createdAt, entity.getCreatedAt());
    }

    @Test
    void shouldMapJpaEntityToDomain() {
        TransactionJpaEntity entity = TransactionJpaEntity.builder()
                .transactionId(UUID.randomUUID())
                .sourceAccountId(UUID.randomUUID())
                .targetAccountId(UUID.randomUUID())
                .amount(BigDecimal.valueOf(500))
                .currency(Currency.BDT)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .createdAt(Instant.now())
                .build();

        Transaction transaction = mapper.toTransactionDomain(entity);

        assertNotNull(transaction);
        assertEquals(new TransactionId(entity.getTransactionId()), transaction.getTransactionId());
        assertEquals(new AccountId(entity.getSourceAccountId()), transaction.getSourceAccountId());
        assertEquals(new AccountId(entity.getTargetAccountId()), transaction.getTargetAccountId());
        assertEquals(new Money(BigDecimal.valueOf(500), Currency.BDT), transaction.getAmount());
        assertEquals(TransactionType.TRANSFER, transaction.getType());
        assertEquals(TransactionStatus.PENDING, transaction.getStatus());
        assertEquals(entity.getCreatedAt(), transaction.getCreatedAt());
    }

    @Test
    void shouldMapDifferentCurrencies() {
        TransactionId transactionId = TransactionId.newId();
        AccountId sourceAccountId = AccountId.newId();
        AccountId targetAccountId = AccountId.newId();
        Money amount = new Money(BigDecimal.valueOf(1000), Currency.USD);
        Instant createdAt = Instant.now();

        Transaction transaction = new Transaction(
                transactionId, sourceAccountId, targetAccountId, amount, createdAt,
                TransactionType.CREDIT, TransactionStatus.COMPLETED
        );

        TransactionJpaEntity entity = mapper.toTransactionJpaEntity(transaction);

        assertEquals(Currency.USD, entity.getCurrency());
        assertEquals(TransactionType.CREDIT, entity.getType());
        assertEquals(TransactionStatus.COMPLETED, entity.getStatus());
    }

    @Test
    void shouldMapAllTransactionTypes() {
        for (TransactionType type : TransactionType.values()) {
            Transaction transaction = new Transaction(
                    TransactionId.newId(),
                    AccountId.newId(),
                    AccountId.newId(),
                    new Money(BigDecimal.valueOf(100), Currency.BDT),
                    Instant.now(),
                    type,
                    TransactionStatus.PENDING
            );

            TransactionJpaEntity entity = mapper.toTransactionJpaEntity(transaction);
            assertEquals(type, entity.getType());
        }
    }

    @Test
    void shouldMapAllTransactionStatuses() {
        for (TransactionStatus status : TransactionStatus.values()) {
            Transaction transaction = new Transaction(
                    TransactionId.newId(),
                    AccountId.newId(),
                    AccountId.newId(),
                    new Money(BigDecimal.valueOf(100), Currency.BDT),
                    Instant.now(),
                    TransactionType.TRANSFER,
                    status
            );

            TransactionJpaEntity entity = mapper.toTransactionJpaEntity(transaction);
            assertEquals(status, entity.getStatus());
        }
    }
}
