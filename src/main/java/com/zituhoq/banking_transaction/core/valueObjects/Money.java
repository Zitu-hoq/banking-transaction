package com.zituhoq.banking_transaction.core.valueObjects;

import com.zituhoq.banking_transaction.core.domain.exception.InvalidAmountException;
import com.zituhoq.banking_transaction.core.domain.exception.NegativeAmountException;

public record Money(Long amount, Currency currency) {

    private boolean isAmountNegative(long amount) {
        return amount < 0;
    }
    public Money add(Money other){
        if (isAmountNegative(other.amount)) throw new NegativeAmountException();
        if(!currency.equals(other.currency)){
            throw new InvalidAmountException("Currency mismatch");
        }
        return new Money(amount+other.amount, currency);
    }

    public Money subtract(Money other){
        if (isAmountNegative(other.amount)) throw new NegativeAmountException();
        if(!currency.equals(other.currency)) throw new InvalidAmountException("Currency mismatch");
        return new Money(amount-other.amount, currency);
    }
}
