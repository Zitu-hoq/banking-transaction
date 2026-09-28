package com.zituhoq.banking_transaction.core.domain;

import com.zituhoq.banking_transaction.core.domain.exception.InsufficientFundsException;
import com.zituhoq.banking_transaction.core.domain.exception.InvalidAmountException;
import com.zituhoq.banking_transaction.core.valueObjects.*;

public class Account {
    private final AccountId accountId;
    private final OwnerId ownerId;
    private Money balance;
    private AccountStatus status;
    private Long version;

    public Account(AccountId accountId, OwnerId ownerId, Money balance, AccountStatus status, Long version){
        this.accountId = accountId;
        this.ownerId = ownerId;
        this.balance = balance;
        this.status = status;
        this.version = version;
    }

    public boolean canDebit(Money amount){
        return balance.amount().compareTo(amount.amount()) >= 0;
    }

    public void debit(Money amount){
        if(!canDebit(amount)){
            throw new InsufficientFundsException();
        }
        balance = balance.subtract(amount);
    }

    public void credit(Money amount){
        balance = balance.add(amount);
    }
}
