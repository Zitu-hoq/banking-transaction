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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransactionRepositoryImplTests {

    @Mock
    private TransactionRepo transactionRepo;

    @Mock
    private TransactionMapper mapper;

    @InjectMocks
    private TransactionRepositoryImpl transactionRepository;

    Transaction transaction;

    @BeforeEach
    void setUp() {
        TransactionId transactionId = TransactionId.newId();
        AccountId sourceAccountId = AccountId.newId();
        AccountId targetAccountId = AccountId.newId();
        Money amount = new Money(BigDecimal.valueOf(500), Currency.BDT);
        Instant createdAt = Instant.now();
        transaction = new Transaction(
                transactionId, sourceAccountId, targetAccountId, amount, createdAt,
                TransactionType.TRANSFER, TransactionStatus.PENDING
        );
    }


    @Test
    void shouldSaveTransaction() {
        transactionRepository.save(transaction);

        verify(mapper).toTransactionJpaEntity(transaction);
        verify(transactionRepo, times(1)).save(any());
    }

    @Test
    void shouldFindByAccountId() {
        AccountId accountId = AccountId.newId();
        Pageable pageable = PageRequest.of(0, 10);

        TransactionJpaEntity entity = TransactionJpaEntity.builder()
                .transactionId(transaction.getTransactionId().id())
                .sourceAccountId(transaction.getSourceAccountId().id())
                .targetAccountId(transaction.getTargetAccountId().id())
                .amount(BigDecimal.valueOf(500))
                .currency(Currency.BDT)
                .type(TransactionType.TRANSFER)
                .status(TransactionStatus.PENDING)
                .createdAt(Instant.now())
                .build();
        Page<TransactionJpaEntity> entityPage = new PageImpl<>(List.of(entity), pageable, 1);

        when(transactionRepo.findByAccountId(accountId.id(), pageable)).thenReturn(entityPage);
        when(mapper.toTransactionDomain(entity)).thenReturn(transaction);

        Page<Transaction> result = transactionRepository.findByAccountId(accountId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(transaction, result.getContent().getFirst());
        verify(transactionRepo).findByAccountId(accountId.id(), pageable);
        verify(mapper).toTransactionDomain(entity);
    }

    @Test
    void shouldReturnEmptyPageWhenNoTransactionsFound() {
        AccountId accountId = AccountId.newId();
        Pageable pageable = PageRequest.of(0, 10);

        Page<TransactionJpaEntity> emptyPage = new PageImpl<>(List.of(), pageable, 0);
        when(transactionRepo.findByAccountId(accountId.id(), pageable)).thenReturn(emptyPage);

        Page<Transaction> result = transactionRepository.findByAccountId(accountId, pageable);

        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());
        verify(transactionRepo).findByAccountId(accountId.id(), pageable);
    }
}
