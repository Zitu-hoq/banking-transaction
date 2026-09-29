package com.zituhoq.banking_transaction.core.valueObjects;

import com.zituhoq.banking_transaction.core.domain.exception.InvalidAmountException;
import com.zituhoq.banking_transaction.core.domain.exception.NegativeAmountException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

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
        accountBalance = new Money(BigDecimal.valueOf(100), currency);
    }

    @Test
    public void shouldAddMoney(){
        Money amount = new Money(BigDecimal.valueOf(10), currency);
        accountBalance = accountBalance.add(amount);
        assertEquals(BigDecimal.valueOf(110), accountBalance.amount(), "Account balance is incorrect");
        assertEquals(currency, accountBalance.currency(), "Current currency is incorrect");
    }

    @Test
    public void shouldSubtractMoney(){
        Money amount = new Money(BigDecimal.valueOf(10), currency);
        accountBalance = accountBalance.subtract(amount);
        assertEquals(BigDecimal.valueOf(90), accountBalance.amount(), "Account balance is incorrect");
        assertEquals(currency, accountBalance.currency(), "Current currency is incorrect");
    }

    @Test
    public void shouldThrowInvalidAmount(){
        Money amount = new Money(BigDecimal.valueOf(10), Currency.USD);
        assertThrows(InvalidAmountException.class, () -> {
            accountBalance.add(amount);
        }, "Add balance exception is incorrect");

        assertThrows(InvalidAmountException.class, () -> {
            accountBalance.subtract(amount);
        }, "Subtract balance exception is incorrect");
    }

    @Test
    public void shouldTrowNegativeAmount(){
        Money amount = new Money(BigDecimal.valueOf(-10), currency);
        assertThrows(NegativeAmountException.class, () -> {
            accountBalance.add(amount);
        });

        assertThrows(NegativeAmountException.class, () -> {
            accountBalance.subtract(amount);
        });
    }
}
