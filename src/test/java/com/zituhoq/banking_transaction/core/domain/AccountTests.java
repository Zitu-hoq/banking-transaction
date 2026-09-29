package com.zituhoq.banking_transaction.core.domain;

import com.zituhoq.banking_transaction.core.domain.exception.InsufficientFundsException;
import com.zituhoq.banking_transaction.core.domain.exception.InvalidAccountStateException;
import com.zituhoq.banking_transaction.core.domain.exception.InvalidAmountException;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.OwnerId;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


public class AccountTests {

    static AccountId accountId;
    static OwnerId ownerId;
    Account initAcc;


    @BeforeAll
    static void preSetUp()
    {
        accountId =  new AccountId(1L);
        ownerId = new OwnerId(5L);
    }

    @BeforeEach
    public void setUp()
    {
        Money initialBalance = new Money(BigDecimal.valueOf(100), Currency.BDT);
        initAcc = new Account(accountId, ownerId, initialBalance, AccountStatus.ACTIVE, 1L);
    }


    @Test
    void shouldAddBalance(){
        Money amount = new Money(BigDecimal.valueOf(100), Currency.BDT);
        initAcc.credit(amount);
        Money expectedBalance = new Money(BigDecimal.valueOf(200), Currency.BDT);
        assertEquals(expectedBalance, initAcc.getBalance(), "Incorrect credit balance");
    }

    @Test
    void shouldDebitBalance(){
        Money amount = new Money(BigDecimal.valueOf(10), Currency.BDT);
        initAcc.debit(amount);
        Money expectedBalance = new Money(BigDecimal.valueOf(90), Currency.BDT);
        assertEquals(expectedBalance, initAcc.getBalance(), "Incorrect debit balance");
    }

    @Test
    void shouldThrowInsufficientFunds()
    {
        Money amount = new Money(BigDecimal.valueOf(200), Currency.BDT);
        assertThrows(InsufficientFundsException.class, ()->{
            initAcc.debit(amount);
        }, "Incorrect Insufficient funds Exception");
    }


    @Test
    void shouldThrowInvalidAccountState()
    {
        initAcc.blockAccount();
        Money amount = new Money(BigDecimal.valueOf(100), Currency.BDT);
        assertThrows(InvalidAccountStateException.class, () -> {
            initAcc.debit(amount);
        }, "Invalid Account State Exception is incorrect");
    }
}
