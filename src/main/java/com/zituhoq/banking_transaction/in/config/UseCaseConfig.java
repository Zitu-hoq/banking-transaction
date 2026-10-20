package com.zituhoq.banking_transaction.in.config;

import com.zituhoq.banking_transaction.core.ports.in.DepositUseCase;
import com.zituhoq.banking_transaction.core.ports.in.GetTransactionHistoryUseCase;
import com.zituhoq.banking_transaction.core.ports.in.TransferMoneyUseCase;
import com.zituhoq.banking_transaction.core.ports.in.WithdrawUseCase;
import com.zituhoq.banking_transaction.core.ports.out.AccountRepository;
import com.zituhoq.banking_transaction.core.ports.out.TransactionRepository;
import com.zituhoq.banking_transaction.core.services.DepositService;
import com.zituhoq.banking_transaction.core.services.GetTransactionHistoryService;
import com.zituhoq.banking_transaction.core.services.TransferMoneyService;
import com.zituhoq.banking_transaction.core.services.WithdrawService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    @Bean
    DepositUseCase depositUseCase(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {
        return new DepositService(
                accountRepository,
                transactionRepository
        );
    }

    @Bean
    WithdrawUseCase withdrawUseCase(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {
        return new WithdrawService(
                accountRepository,
                transactionRepository
        );
    }

    @Bean
    TransferMoneyUseCase transferMoneyUseCase(
            AccountRepository accountRepository,
            TransactionRepository transactionRepository
    ) {
        return new TransferMoneyService(
                accountRepository,
                transactionRepository
        );
    }

    @Bean
    GetTransactionHistoryUseCase getTransactionHistoryUseCase(
            TransactionRepository transactionRepository
    ) {
        return new GetTransactionHistoryService(
                transactionRepository
        );
    }
}
