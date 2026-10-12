package com.zituhoq.banking_transaction.core.ports.in;

import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetTransactionHistoryUseCase {
    Page<TransactionResult> getTransactionHistory(AccountId accountId, Pageable pageable);
}
