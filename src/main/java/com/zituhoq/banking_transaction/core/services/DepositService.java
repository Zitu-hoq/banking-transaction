package com.zituhoq.banking_transaction.core.services;

import com.zituhoq.banking_transaction.core.domain.*;
import com.zituhoq.banking_transaction.core.domain.exception.AccountNotFoundException;
import com.zituhoq.banking_transaction.core.domain.exception.InvalidAccountStateException;
import com.zituhoq.banking_transaction.core.ports.in.DepositUseCase;
import com.zituhoq.banking_transaction.core.ports.out.AccountRepository;
import com.zituhoq.banking_transaction.core.ports.out.TransactionRepository;
import com.zituhoq.banking_transaction.core.services.dto.DepositCommand;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;

import java.time.Instant;

public class DepositService implements DepositUseCase {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public DepositService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public TransactionResult deposit(DepositCommand command) {
        //load account
        Account account = accountRepository.findById(command.accountId())
                .orElseThrow(()-> new AccountNotFoundException("Account not found"));
        // perform deposit operation
        account.credit(command.amount());

        // update account
        accountRepository.save(account);

        // create transaction and persist it
        Transaction tx = new Transaction(
                TransactionId.newId(),
                SystemAccounts.SYSTEM,
                command.accountId(),
                command.amount(),
                Instant.now(),
                TransactionType.CREDIT,
                TransactionStatus.COMPLETED
        );
        transactionRepository.save(tx);

        // return unified result
        return new TransactionResult(
                tx.getTransactionId(),
                tx.getSourceAccountId(),
                tx.getTargetAccountId(),
                tx.getAmount(),
                tx.getType(),
                tx.getStatus(),
                tx.getCreatedAt()
        );
    }
}
