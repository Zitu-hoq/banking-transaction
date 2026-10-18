package com.zituhoq.banking_transaction.core.services;

import com.zituhoq.banking_transaction.core.domain.*;
import com.zituhoq.banking_transaction.core.domain.exception.AccountNotFoundException;
import com.zituhoq.banking_transaction.core.domain.exception.InsufficientFundsException;
import com.zituhoq.banking_transaction.core.domain.exception.SameAccountTransferException;
import com.zituhoq.banking_transaction.core.ports.out.AccountRepository;
import com.zituhoq.banking_transaction.core.ports.out.TransactionRepository;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.services.dto.TransferCommand;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TransferMoneyServiceTest {

    @Mock
    AccountRepository accountRepository;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransferMoneyService transferMoneyService;

    Account source;
    Account target;
    TransferCommand command;

    @BeforeEach
    void setUp() {
        source = new Account(AccountId.newId(), new OwnerId(5L), new Money(BigDecimal.valueOf(1000), Currency.BDT), AccountStatus.ACTIVE, 1L);
        target = new Account(AccountId.newId(),new OwnerId(10L), new Money(BigDecimal.valueOf(500), Currency.BDT), AccountStatus.ACTIVE, 1L);
        command = new TransferCommand(
                source.getAccountId(), target.getAccountId(), new Money(BigDecimal.valueOf(100), Currency.BDT), "1", "X-1"
        );
    }

    @Test
    void shouldTransferMoney() {
        // creat transfer command
        Money amount = new Money(BigDecimal.valueOf(100), Currency.BDT);

        // mock accounts
        when(accountRepository.findById(source.getAccountId())).thenReturn(Optional.of(source));
        when(accountRepository.findById(target.getAccountId())).thenReturn(Optional.of(target));

        //actual function call
        TransactionResult result = transferMoneyService.transfer(command);


        // expected source and target account state
        source.debit(amount);
        target.credit(amount);

        // verify db calls
        verify(accountRepository).save(source);
        verify(accountRepository).save(target);

        // mockito argument capturer obj type "Transaction"
        ArgumentCaptor<Transaction> txCaptor = ArgumentCaptor.forClass(Transaction.class);

        // capture the argument when save is called
        verify(transactionRepository).save(txCaptor.capture());
        // get the captured value/obj
        Transaction savedTx = txCaptor.getValue();

        // checking the captured values with expected values(here for transaction)
        assertThat(savedTx.getSourceAccountId()).isEqualTo(source.getAccountId());
        assertThat(savedTx.getTargetAccountId()).isEqualTo(target.getAccountId());
        assertThat(savedTx.getAmount().amount()).isEqualByComparingTo("100");
        assertThat(savedTx.getType()).isEqualTo(TransactionType.TRANSFER);
        assertThat(savedTx.getStatus()).isEqualTo(TransactionStatus.COMPLETED);

        // checking the transactionResult obj
        assertThat(result.transactionId()).isEqualTo(savedTx.getTransactionId());
        assertThat(result.amount()).isEqualTo(amount);
        assertThat(result.targetAccountId()).isEqualTo(target.getAccountId());
        assertThat(result.sourceAccountId()).isEqualTo(source.getAccountId());
        assertThat(result.type()).isEqualTo(TransactionType.TRANSFER);
    }


    @Test
    void shouldThrowAccountNotFoundExceptionForSource() {
        // mock unexisted source account
        when(accountRepository.findById(source.getAccountId())).thenReturn(Optional.empty());

        // expect
        assertThrows(AccountNotFoundException.class, () -> transferMoneyService.transfer(command));


        // verify db calls
        verify(accountRepository).findById(source.getAccountId());
        verify(accountRepository, never()).findById(target.getAccountId());
        verify(accountRepository, never()).save(source);
    }

    @Test
    void shouldThrowAccountNotFoundExceptionForTarget() {

        // mock source account
        when(accountRepository.findById(source.getAccountId())).thenReturn(Optional.of(source));
        // mock unexisted target account
        when(accountRepository.findById(target.getAccountId())).thenReturn(Optional.empty());

        // expect
        assertThrows(AccountNotFoundException.class, () -> transferMoneyService.transfer(command));


        // verify db calls
        verify(accountRepository).findById(source.getAccountId());
        verify(accountRepository).findById(target.getAccountId());
        verify(accountRepository, never()).save(source);
    }

    @Test
    void shouldThrowSameAccountTransferException(){
        TransferCommand sameSourceAndTargetCommand = new TransferCommand(
                source.getAccountId(), source.getAccountId(), new Money(BigDecimal.valueOf(100), Currency.BDT), "1", "X-1"
        );

        // expect
        assertThrows(SameAccountTransferException.class, () -> transferMoneyService.transfer(sameSourceAndTargetCommand));

        // verify no db call
        verify(accountRepository, never()).findById(source.getAccountId());
    }


    @Test
    void shouldThrowInsufficientFundsException(){
        // create transfer command
        TransferCommand cmd = new TransferCommand(
                source.getAccountId(), target.getAccountId(), new Money(BigDecimal.valueOf(2000), Currency.BDT), "1", "X-1"
        );

        // mock accounts
        when(accountRepository.findById(source.getAccountId())).thenReturn(Optional.of(source));
        when(accountRepository.findById(target.getAccountId())).thenReturn(Optional.of(target));

        //expect
        assertThrows(InsufficientFundsException.class, () -> transferMoneyService.transfer(cmd));

        // verify db calls
        verify(accountRepository).findById(source.getAccountId());
        verify(accountRepository).findById(target.getAccountId());
        verify(accountRepository, never()).save(source);
        verify(accountRepository, never()).save(target);
    }
}
