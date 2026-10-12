package com.zituhoq.banking_transaction.core.ports.in;

import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.services.dto.TransferCommand;

public interface TransferMoneyUseCase {
    TransactionResult transfer(TransferCommand command);
}
