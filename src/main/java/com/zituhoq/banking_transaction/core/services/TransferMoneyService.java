package com.zituhoq.banking_transaction.core.services;

import com.zituhoq.banking_transaction.core.domain.*;
import com.zituhoq.banking_transaction.core.domain.exception.AccountNotFoundException;
import com.zituhoq.banking_transaction.core.domain.exception.SameAccountTransferException;
import com.zituhoq.banking_transaction.core.ports.in.TransferMoneyUseCase;
import com.zituhoq.banking_transaction.core.ports.out.AccountRepository;
import com.zituhoq.banking_transaction.core.ports.out.TransactionRepository;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.services.dto.TransferCommand;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;

import java.time.Instant;

public class TransferMoneyService implements TransferMoneyUseCase {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public TransferMoneyService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public TransactionResult transfer(TransferCommand command) {

        //check for same account
        if(command.sourceAccountId().equals(command.targetAccountId())) throw new SameAccountTransferException();

        // find account by source id and target id
        Account source = accountRepository.findById(command.sourceAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Source account not found"));

        Account target = accountRepository.findById(command.targetAccountId())
                .orElseThrow(() -> new AccountNotFoundException("Target account not found"));

        // transfer money and credit target account, debit source account
        source.debit(command.amount());
        target.credit(command.amount());

        //persist update
        accountRepository.save(source);
        accountRepository.save(target);

        // create and persist transaction
        Transaction tx = new Transaction(
                TransactionId.newId(),
                command.sourceAccountId(),
                command.targetAccountId(),
                command.amount(),
                Instant.now(),
                TransactionType.TRANSFER,
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
