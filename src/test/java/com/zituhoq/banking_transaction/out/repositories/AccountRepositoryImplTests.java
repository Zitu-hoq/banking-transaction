package com.zituhoq.banking_transaction.out.repositories;

import com.zituhoq.banking_transaction.core.domain.Account;
import com.zituhoq.banking_transaction.core.domain.AccountStatus;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.OwnerId;
import com.zituhoq.banking_transaction.out.database.AccountRepo;
import com.zituhoq.banking_transaction.out.database.entities.AccountJpaEntity;
import com.zituhoq.banking_transaction.out.database.mappers.AccountMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AccountRepositoryImplTests {

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private AccountMapper mapper;

    private AccountRepositoryImpl accountRepository;
    private AutoCloseable mocks;

    @BeforeEach
    void setUp() {
        mocks = MockitoAnnotations.openMocks(this);
        accountRepository = new AccountRepositoryImpl(accountRepo, mapper);
    }

    @org.junit.jupiter.api.AfterEach
    void tearDown() throws Exception {
        mocks.close();
    }

    @Test
    void shouldFindById() {
        AccountId accountId = new AccountId(1L);
        AccountJpaEntity entity = AccountJpaEntity.builder()
                .accountId(1L)
                .ownerId(5L)
                .balance(BigDecimal.valueOf(100))
                .currency(Currency.BDT)
                .status(AccountStatus.ACTIVE)
                .version(1L)
                .build();
        Account account = new Account(
                accountId,
                new OwnerId(5L),
                new Money(BigDecimal.valueOf(100), Currency.BDT),
                AccountStatus.ACTIVE,
                1L
        );

        when(accountRepo.findById(accountId.id())).thenReturn(Optional.of(entity));
        when(mapper.toAccountDomain(entity)).thenReturn(account);

        Optional<Account> result = accountRepository.findById(accountId);

        assertTrue(result.isPresent());
        assertEquals(account, result.get());
        verify(accountRepo).findById(accountId.id());
        verify(mapper).toAccountDomain(entity);
    }

    @Test
    void shouldReturnEmptyWhenAccountNotFound() {
        AccountId accountId = new AccountId(99L);

        when(accountRepo.findById(accountId.id())).thenReturn(Optional.empty());

        Optional<Account> result = accountRepository.findById(accountId);

        assertTrue(result.isEmpty());
        verify(accountRepo).findById(accountId.id());
        verify(mapper, never()).toAccountDomain(any());
    }

    @Test
    void shouldCheckExistsById() {
        AccountId accountId = new AccountId(1L);

        when(accountRepo.existsById(accountId.id())).thenReturn(true);

        boolean exists = accountRepository.existsById(accountId);

        assertTrue(exists);
        verify(accountRepo).existsById(accountId.id());
    }

    @Test
    void shouldReturnFalseWhenAccountDoesNotExist() {
        AccountId accountId = new AccountId(99L);

        when(accountRepo.existsById(accountId.id())).thenReturn(false);

        boolean exists = accountRepository.existsById(accountId);

        assertFalse(exists);
        verify(accountRepo).existsById(accountId.id());
    }

    @Test
    void shouldSaveAccount() {
        AccountId accountId = new AccountId(1L);
        OwnerId ownerId = new OwnerId(5L);
        Money balance = new Money(BigDecimal.valueOf(100), Currency.BDT);

        Account account = new Account(accountId, ownerId, balance, AccountStatus.ACTIVE, 1L);

        AccountJpaEntity entity = AccountJpaEntity.builder()
                .accountId(1L)
                .ownerId(5L)
                .balance(BigDecimal.valueOf(100))
                .currency(Currency.BDT)
                .status(AccountStatus.ACTIVE)
                .version(1L)
                .build();
        AccountJpaEntity savedEntity = AccountJpaEntity.builder()
                .accountId(1L)
                .ownerId(5L)
                .balance(BigDecimal.valueOf(100))
                .currency(Currency.BDT)
                .status(AccountStatus.ACTIVE)
                .version(2L)
                .build();
        Account savedAccount = new Account(accountId, ownerId, balance, AccountStatus.ACTIVE, 2L);

        when(mapper.toAccountJpaEntity(account)).thenReturn(entity);
        when(accountRepo.save(entity)).thenReturn(savedEntity);
        when(mapper.toAccountDomain(savedEntity)).thenReturn(savedAccount);

        Account result = accountRepository.save(account);

        assertEquals(savedAccount, result);
        verify(mapper).toAccountJpaEntity(account);
        verify(accountRepo).save(entity);
        verify(mapper).toAccountDomain(savedEntity);
    }
}
