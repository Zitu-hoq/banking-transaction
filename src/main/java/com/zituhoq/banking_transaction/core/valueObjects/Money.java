package com.zituhoq.banking_transaction.core.valueObjects;

import com.zituhoq.banking_transaction.core.domain.exception.InvalidAmountException;
import com.zituhoq.banking_transaction.core.domain.exception.NegativeAmountException;

import java.math.BigDecimal;

public record Money(BigDecimal amount, Currency currency) {

    private boolean isAmountNegative(BigDecimal amount) {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
    public Money add(Money other){
        if (isAmountNegative(other.amount)) throw new NegativeAmountException();
        if(!currency.equals(other.currency)){
            throw new InvalidAmountException("Currency mismatch");
        }
        return new Money(amount.add(other.amount), currency);
    }

    public Money subtract(Money other){
        if (isAmountNegative(other.amount)) throw new NegativeAmountException();
        if(!currency.equals(other.currency)) throw new InvalidAmountException("Currency mismatch");
        return new Money(amount.subtract(other.amount), currency);
    }
}
