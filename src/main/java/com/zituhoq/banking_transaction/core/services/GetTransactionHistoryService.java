package com.zituhoq.banking_transaction.core.services;

import com.zituhoq.banking_transaction.core.ports.in.GetTransactionHistoryUseCase;
import com.zituhoq.banking_transaction.core.ports.out.AccountRepository;
import com.zituhoq.banking_transaction.core.ports.out.TransactionRepository;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class GetTransactionHistoryService implements GetTransactionHistoryUseCase {

    private final TransactionRepository transactionRepository;

    public GetTransactionHistoryService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public Page<TransactionResult> getTransactionHistory(AccountId accountId, Pageable pageable) {
        // find and return results
        return transactionRepository.findByAccountId(accountId, pageable)
                .map(tx -> new TransactionResult(
                        tx.getTransactionId(),
                        tx.getSourceAccountId(),
                        tx.getTargetAccountId(),
                        tx.getAmount(),
                        tx.getType(),
                        tx.getStatus(),
                        tx.getCreatedAt()
                ));
    }
}
