package com.zituhoq.banking_transaction.core.ports.in;

import com.zituhoq.banking_transaction.core.services.dto.DepositCommand;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;

public interface DepositUseCase {
    TransactionResult deposit(DepositCommand command);
}
