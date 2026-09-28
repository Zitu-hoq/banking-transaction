package com.zituhoq.banking_transaction.core.valueObjects;

import com.zituhoq.banking_transaction.core.domain.exception.InvalidAmountException;
import com.zituhoq.banking_transaction.core.domain.exception.NegativeAmountException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyTests {

    private static Currency currency;
    private Money accountBalance;
    @BeforeAll
    static void initCurrency() {
        currency = Currency.BDT;
    }

    @BeforeEach
    void initAmount() {
        accountBalance = new Money(100L, currency);
    }

    @Test
    public void shouldAddMoney(){
        Money amount = new Money(10L, currency);
        accountBalance = accountBalance.add(amount);
        assertEquals(110L, accountBalance.amount(), "Account balance is incorrect");
        assertEquals(currency, accountBalance.currency(), "Current currency is incorrect");
    }

    @Test
    public void shouldSubtractMoney(){
        Money amount = new Money(10L, currency);
        accountBalance = accountBalance.subtract(amount);
        assertEquals(90L, accountBalance.amount(), "Account balance is incorrect");
        assertEquals(currency, accountBalance.currency(), "Current currency is incorrect");
    }

    @Test
    public void shouldThrowInvalidAmount(){
        Money amount = new Money(10L, Currency.USD);
        assertThrows(InvalidAmountException.class, () -> {
            accountBalance.add(amount);
        }, "Add balance exception is incorrect");

        assertThrows(InvalidAmountException.class, () -> {
            accountBalance.subtract(amount);
        }, "Subtract balance exception is incorrect");
    }

    @Test
    public void shouldTrowNegativeAmount(){
        Money amount = new Money(-10L, currency);
        assertThrows(NegativeAmountException.class, () -> {
            accountBalance.add(amount);
        });

        assertThrows(NegativeAmountException.class, () -> {
            accountBalance.subtract(amount);
        });
    }
}
