package com.zituhoq.banking_transaction.core.ports.in;

import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.services.dto.WithdrawCommand;

public interface WithdrawUseCase {
    TransactionResult withdraw(WithdrawCommand command);
}
