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
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountRepositoryImplTests {

    @Mock
    private AccountRepo accountRepo;

    @Mock
    private AccountMapper mapper;

    @InjectMocks
    private AccountRepositoryImpl accountRepository;

    Account account;

    @BeforeEach
    public void setUp() {
        AccountId accountId = AccountId.newId();
        OwnerId ownerId = new OwnerId(5L);
        Money balance = new Money(BigDecimal.valueOf(100), Currency.BDT);
        account = new Account(accountId, ownerId, balance, AccountStatus.ACTIVE, 1L);
    }

    @Test
    void shouldFindById() {
        AccountId accountId = AccountId.newId();
        AccountJpaEntity entity = AccountJpaEntity.builder()
                .accountId(accountId.id())
                .ownerId(5L)
                .balance(BigDecimal.valueOf(100))
                .currency(Currency.BDT)
                .status(AccountStatus.ACTIVE)
                .version(1L)
                .build();

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
        AccountId accountId = AccountId.newId();

        when(accountRepo.findById(accountId.id())).thenReturn(Optional.empty());

        Optional<Account> result = accountRepository.findById(accountId);

        assertTrue(result.isEmpty());
        verify(accountRepo).findById(accountId.id());
        verify(mapper, never()).toAccountDomain(any());
    }

    @Test
    void shouldCheckExistsById() {
        AccountId accountId = AccountId.newId();

        when(accountRepo.existsById(accountId.id())).thenReturn(true);

        boolean exists = accountRepository.existsById(accountId);

        assertTrue(exists);
        verify(accountRepo).existsById(accountId.id());
    }

    @Test
    void shouldReturnFalseWhenAccountDoesNotExist() {
        AccountId accountId = AccountId.newId();

        when(accountRepo.existsById(accountId.id())).thenReturn(false);

        boolean exists = accountRepository.existsById(accountId);

        assertFalse(exists);
        verify(accountRepo).existsById(accountId.id());
    }

    @Test
    void shouldInvokeAllMethodsInsideSaveAndReturnSameAccount() {

        when(mapper.toAccountDomain(any())).thenReturn(account);

        Account result = accountRepository.save(account);

        assertEquals(account, result, "result mismatch");
        verify(mapper).toAccountJpaEntity(account);
        verify(accountRepo, times(1)).save(any());
        verify(mapper).toAccountDomain(any());
    }
}
