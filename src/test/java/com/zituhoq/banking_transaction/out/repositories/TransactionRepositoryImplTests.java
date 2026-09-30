package com.zituhoq.banking_transaction.out.repositories;

import com.zituhoq.banking_transaction.core.domain.Transaction;
import com.zituhoq.banking_transaction.core.domain.TransactionStatus;
import com.zituhoq.banking_transaction.core.domain.TransactionType;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;
import com.zituhoq.banking_transaction.out.database.TransactionRepo;
import com.zituhoq.banking_transaction.out.database.entities.TransactionJpaEntity;
import com.zituhoq.banking_transaction.out.database.mappers.TransactionMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TransactionRepositoryImplTests {

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private TransactionMapper mapper;

    private TransactionRepositoryImpl transactionRepository;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        transactionRepository = new TransactionRepositoryImpl(mapper, transactionRepo);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldSaveTransaction() {
        TransactionId transactionId = new TransactionId(1L);
        AccountId sourceAccountId = new AccountId(10L);
        AccountId targetAccountId = new AccountId(20L);
        Money amount = new Money(BigDecimal.valueOf(500), Currency.BDT);
        Instant createdAt = Instant.now();

        Transaction transaction = new Transaction(
                transactionId, sourceAccountId, targetAccountId, amount, createdAt,
                TransactionType.TRANSFER, TransactionStatus.PENDING
        );

        TransactionJpaEntity entity = TransactionJpaEntity.builder()
                .transactionId(1L)
                .sourceAccountId(10L)
                .targetAccountId(20L)
                .amount(BigDecimal.valueOf(500))
                .currency(Currency.BDT)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .createdAt(createdAt)
                .build();
        when(mapper.toTransactionJpaEntity(transaction)).thenReturn(entity);

        transactionRepository.save(transaction);

        verify(mapper).toTransactionJpaEntity(transaction);
        verify(transactionRepo).save(entity);
    }

    @Test
    void shouldFindByAccountId() {
        AccountId accountId = new AccountId(10L);
        Pageable pageable = PageRequest.of(0, 10);

        TransactionJpaEntity entity = TransactionJpaEntity.builder()
                .transactionId(1L)
                .sourceAccountId(10L)
                .targetAccountId(20L)
                .amount(BigDecimal.valueOf(500))
                .currency(Currency.BDT)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .createdAt(Instant.now())
                .build();
        Page<TransactionJpaEntity> entityPage = new PageImpl<>(List.of(entity), pageable, 1);

        TransactionId transactionId = new TransactionId(1L);
        AccountId sourceAccountId = new AccountId(10L);
        AccountId targetAccountId = new AccountId(20L);
        Money amount = new Money(BigDecimal.valueOf(500), Currency.BDT);
        Instant createdAt = Instant.now();

        Transaction transaction = new Transaction(
                transactionId, sourceAccountId, targetAccountId, amount, createdAt,
                TransactionType.TRANSFER, TransactionStatus.PENDING
        );

        when(transactionRepo.findByAccountId(accountId.id(), pageable)).thenReturn(entityPage);
        when(mapper.toTransactionDomain(entity)).thenReturn(transaction);

        Page<Transaction> result = transactionRepository.findByAccountId(accountId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(transaction, result.getContent().get(0));
        verify(transactionRepo).findByAccountId(accountId.id(), pageable);
        verify(mapper).toTransactionDomain(entity);
    }

    @Test
    void shouldReturnEmptyPageWhenNoTransactionsFound() {
        AccountId accountId = new AccountId(99L);
        Pageable pageable = PageRequest.of(0, 10);

        Page<TransactionJpaEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(transactionRepo.findByAccountId(accountId.id(), pageable)).thenReturn(emptyPage);

        Page<Transaction> result = transactionRepository.findByAccountId(accountId, pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
    }
}
