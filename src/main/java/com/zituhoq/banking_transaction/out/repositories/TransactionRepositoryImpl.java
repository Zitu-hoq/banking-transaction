package com.zituhoq.banking_transaction.out.repositories;

import com.zituhoq.banking_transaction.core.domain.Transaction;
import com.zituhoq.banking_transaction.core.ports.out.TransactionRepository;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.out.database.TransactionRepo;
import com.zituhoq.banking_transaction.out.database.entities.TransactionJpaEntity;
import com.zituhoq.banking_transaction.out.database.mappers.TransactionMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class TransactionRepositoryImpl implements TransactionRepository {

    private final TransactionRepo transactionRepo;
    private final TransactionMapper mapper;

    public TransactionRepositoryImpl(TransactionMapper mapper, TransactionRepo transactionRepo) {
        this.mapper = mapper;
        this.transactionRepo = transactionRepo;
    }

    @Override
    public void save(Transaction transaction) {
        TransactionJpaEntity entity = mapper.toTransactionJpaEntity(transaction);
        transactionRepo.save(entity);
    }

    @Override
    public Page<Transaction> findByAccountId(AccountId id, Pageable pageable) {
        Page<TransactionJpaEntity> page = transactionRepo.findByAccountId(id.id(), pageable);
        return page.map(mapper::toTransactionDomain);
    }
}
