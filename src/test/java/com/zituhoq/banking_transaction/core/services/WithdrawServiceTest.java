package com.zituhoq.banking_transaction.core.services;


import com.zituhoq.banking_transaction.core.domain.*;
import com.zituhoq.banking_transaction.core.domain.exception.AccountNotFoundException;
import com.zituhoq.banking_transaction.core.ports.out.AccountRepository;
import com.zituhoq.banking_transaction.core.ports.out.TransactionRepository;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.services.dto.WithdrawCommand;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.OwnerId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WithdrawServiceTest {
    @Mock
    private AccountRepository accountRepository;
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private WithdrawService withdrawService;

    Account initAcc;


    @BeforeEach
    public void setUp() {
        AccountId accountId = AccountId.newId();
        OwnerId ownerId = new OwnerId(5L);
        Money balance = new Money(BigDecimal.valueOf(500), Currency.BDT);
        initAcc = new Account(accountId, ownerId, balance, AccountStatus.ACTIVE, 1L);
    }

    @Test
    void deposit_shouldCreditAccountAndSaveTransaction() {

        //making withdraw command
        Money withdrawAmount = new Money(BigDecimal.valueOf(100), Currency.BDT);
        WithdrawCommand command = new WithdrawCommand(initAcc.getAccountId(), withdrawAmount, "1", "11");

        // mocking repository search
        when(accountRepository.findById(initAcc.getAccountId())).thenReturn(Optional.of(initAcc));

        // actual test call
        TransactionResult result = withdrawService.withdraw(command);

        // changing initAcc according to result
        initAcc.credit(withdrawAmount);

        // verify persist call
        verify(accountRepository).save(initAcc);

        // mockito argument capturer obj type "Transaction"
        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);

        // capture the argument when save is called
        verify(transactionRepository).save(txCaptor.capture());
        // get the captured value/obj
        Transaction savedTx = txCaptor.getValue();

        // checking the captured values with expected values(here for transaction)
        assertThat(savedTx.getSourceAccountId()).isEqualTo(initAcc.getAccountId());
        assertThat(savedTx.getTargetAccountId()).isEqualTo(SystemAccounts.CASH);
        assertThat(savedTx.getAmount().amount()).isEqualByComparingTo("100");
        assertThat(savedTx.getType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(savedTx.getStatus()).isEqualTo(TransactionStatus.COMPLETED);

        // checking the transactionResult obj
        assertThat(result.transactionId()).isEqualTo(savedTx.getTransactionId());
        assertThat(result.amount()).isEqualTo(withdrawAmount);
    }

    @Test
    void deposit_shouldThrowIfAccountNotFound() {
        when(accountRepository.findById(initAcc.getAccountId())).thenReturn(Optional.empty());

        WithdrawCommand command = new WithdrawCommand(initAcc.getAccountId(), new Money(BigDecimal.valueOf(100), Currency.BDT), "1", "11");

        // expect
        assertThrows(AccountNotFoundException.class,
                () -> withdrawService.withdraw(command));

        verify(accountRepository, never()).save(initAcc);
        verify(transactionRepository, never()).save(any());
    }
}
