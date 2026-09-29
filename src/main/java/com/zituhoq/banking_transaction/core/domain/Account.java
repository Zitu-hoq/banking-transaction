package com.zituhoq.banking_transaction.core.domain;

import com.zituhoq.banking_transaction.core.domain.exception.InsufficientFundsException;
import com.zituhoq.banking_transaction.core.domain.exception.InvalidAccountStateException;
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

    private boolean canDebit(Money amount){
        return balance.amount().compareTo(amount.amount()) >= 0;
    }

    private boolean isInactive(AccountStatus status){
        return !status.equals(AccountStatus.ACTIVE);
    }

    public void debit(Money amount){
        if(isInactive(status)) throw new InvalidAccountStateException();
        if(!canDebit(amount)){
            throw new InsufficientFundsException();
        }
        balance = balance.subtract(amount);
    }

    public void credit(Money amount){
        if(isInactive(status)) throw new InvalidAccountStateException();
        balance = balance.add(amount);
    }

    //change account status
    public void activateAccount(){
        this.status = AccountStatus.ACTIVE;
    }

    public void blockAccount(){
        this.status = AccountStatus.BLOCKED;
    }

    public void closeAccount(){
        this.status = AccountStatus.CLOSED;
    }


    // getters
    public AccountId getAccountId() {
        return accountId;
    }

    public OwnerId getOwnerId() {
        return ownerId;
    }

    public Money getBalance() {
        return balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public Long getVersion() {
        return version;
    }

}
