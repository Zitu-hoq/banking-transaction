package com.zituhoq.banking_transaction.out.database.mappers;

import com.zituhoq.banking_transaction.core.domain.Account;
import com.zituhoq.banking_transaction.core.domain.AccountStatus;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.OwnerId;
import com.zituhoq.banking_transaction.out.database.entities.AccountJpaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class AccountMapperTests {

    private AccountMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new AccountMapperImpl();
    }

    @Test
    void shouldMapDomainToJpaEntity() {
        AccountId accountId = new AccountId(1L);
        OwnerId ownerId = new OwnerId(5L);
        Money balance = new Money(BigDecimal.valueOf(1000), Currency.BDT);

        Account account = new Account(accountId, ownerId, balance, AccountStatus.ACTIVE, 1L);

        AccountJpaEntity entity = mapper.toAccountJpaEntity(account);

        assertNotNull(entity);
        assertEquals(ownerId.id(), entity.getOwnerId());
        assertEquals(balance.amount(), entity.getBalance());
        assertEquals(balance.currency(), entity.getCurrency());
        assertEquals(AccountStatus.ACTIVE, entity.getStatus());
    }

    @Test
    void shouldMapJpaEntityToDomain() {
        AccountJpaEntity entity = AccountJpaEntity.builder()
                .accountId(1L)
                .ownerId(5L)
                .balance(BigDecimal.valueOf(1000))
                .currency(Currency.BDT)
                .status(AccountStatus.ACTIVE)
                .version(1L)
                .build();

        Account account = mapper.toAccountDomain(entity);

        assertNotNull(account);
        assertEquals(new AccountId(1L), account.getAccountId());
        assertEquals(new OwnerId(5L), account.getOwnerId());
        assertEquals(new Money(BigDecimal.valueOf(1000), Currency.BDT), account.getBalance());
        assertEquals(AccountStatus.ACTIVE, account.getStatus());
        assertEquals(1L, account.getVersion());
    }

    @Test
    void shouldMapDifferentCurrencies() {
        Account account = new Account(
                new AccountId(2L),
                new OwnerId(10L),
                new Money(BigDecimal.valueOf(500), Currency.USD),
                AccountStatus.ACTIVE,
                1L
        );

        AccountJpaEntity entity = mapper.toAccountJpaEntity(account);

        assertEquals(Currency.USD, entity.getCurrency());
    }

    @Test
    void shouldMapAllAccountStatuses() {
        for (AccountStatus status : AccountStatus.values()) {
            Account account = new Account(
                    new AccountId(1L),
                    new OwnerId(5L),
                    new Money(BigDecimal.valueOf(100), Currency.BDT),
                    status,
                    1L
            );

            AccountJpaEntity entity = mapper.toAccountJpaEntity(account);
            assertEquals(status, entity.getStatus());
        }
    }

    @Test
    void shouldMapZeroBalance() {
        Account account = new Account(
                new AccountId(1L),
                new OwnerId(5L),
                new Money(BigDecimal.ZERO, Currency.BDT),
                AccountStatus.ACTIVE,
                1L
        );

        AccountJpaEntity entity = mapper.toAccountJpaEntity(account);

        assertEquals(BigDecimal.ZERO, entity.getBalance());
    }

    @Test
    void shouldMapLargeBalance() {
        Account account = new Account(
                new AccountId(1L),
                new OwnerId(5L),
                new Money(BigDecimal.valueOf(999999999.99), Currency.BDT),
                AccountStatus.ACTIVE,
                1L
        );

        AccountJpaEntity entity = mapper.toAccountJpaEntity(account);

        assertEquals(BigDecimal.valueOf(999999999.99), entity.getBalance());
    }
}
