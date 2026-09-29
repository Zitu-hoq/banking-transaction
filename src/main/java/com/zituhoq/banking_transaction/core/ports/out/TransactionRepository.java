package com.zituhoq.banking_transaction.core.ports.out;

import com.zituhoq.banking_transaction.core.domain.Transaction;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface TransactionRepository {
    void save(Transaction transaction);
    Page<Transaction> findByAccountId(AccountId id, Pageable pageable);
}
