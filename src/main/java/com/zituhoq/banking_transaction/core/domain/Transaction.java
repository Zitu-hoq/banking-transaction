package com.zituhoq.banking_transaction.core.domain;

import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;

import java.time.Instant;

public class Transaction {
    private final TransactionId transactionId;
    private final AccountId sourceAccountId;
    private final AccountId targetAccountId;
    private final Money amount;
    private TransactionType type;
    private TransactionStatus status;
    private final Instant createdAt;

    public Transaction(TransactionId transactionId, AccountId sourceAccountId, AccountId targetAccountId, Money amount, Instant createdAt, TransactionType type, TransactionStatus status) {
        this.transactionId = transactionId;
        this.sourceAccountId = sourceAccountId;
        this.targetAccountId = targetAccountId;
        this.amount = amount;
        this.createdAt = createdAt;
        this.type = type;
        this.status = status;
    }

    public void markAsCompleted() {
        this.status = TransactionStatus.COMPLETED;
    }
    public void markAsFailed() {
        this.status = TransactionStatus.FAILED;
    }

    //  getters

    public TransactionId getTransactionId() {
        return transactionId;
    }

    public AccountId getSourceAccountId() {
        return sourceAccountId;
    }

    public AccountId getTargetAccountId() {
        return targetAccountId;
    }

    public Money getAmount() {
        return amount;
    }

    public TransactionType getType() {
        return type;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
