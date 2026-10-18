package com.zituhoq.banking_transaction.core.services;

import com.zituhoq.banking_transaction.core.domain.*;
import com.zituhoq.banking_transaction.core.domain.exception.InvalidAccountStateException;
import com.zituhoq.banking_transaction.core.ports.in.WithdrawUseCase;
import com.zituhoq.banking_transaction.core.ports.out.AccountRepository;
import com.zituhoq.banking_transaction.core.ports.out.TransactionRepository;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.services.dto.WithdrawCommand;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;

import java.time.Instant;

public class WithdrawService implements WithdrawUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public WithdrawService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public TransactionResult withdraw(WithdrawCommand command) {
        //load account
        Account account = accountRepository.findById(command.accountId())
                .orElseThrow(InvalidAccountStateException::new);

        // perform withdraw operation
        account.debit(command.amount());

        // update account
        accountRepository.save(account);

        // create transaction and persist it
        Transaction tx = new Transaction(
                TransactionId.newId(),
                command.accountId(),
                SystemAccounts.CASH,
                command.amount(),
                Instant.now(),
                TransactionType.DEBIT,
                TransactionStatus.COMPLETED
        );
        transactionRepository.save(tx);

        //return unified result
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
