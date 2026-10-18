package com.zituhoq.banking_transaction.core.services;

import com.zituhoq.banking_transaction.core.domain.SystemAccounts;
import com.zituhoq.banking_transaction.core.domain.Transaction;
import com.zituhoq.banking_transaction.core.domain.TransactionStatus;
import com.zituhoq.banking_transaction.core.domain.TransactionType;
import com.zituhoq.banking_transaction.core.ports.out.TransactionRepository;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetTransactionHistoryServiceTest {
    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private GetTransactionHistoryService transactionHistoryService;
    // <-- replace with the actual class that implements getTransactionHistory

    @Test
    void shouldGetTransactionHistory() {
        // create transaction history arguments
        AccountId accountId = AccountId.newId();
        Pageable pageable = PageRequest.of(0,10);

        // create list of transactions
        Transaction tx = new Transaction(
                TransactionId.newId(),
                SystemAccounts.SYSTEM,
                accountId,
                new Money(BigDecimal.valueOf(200), Currency.BDT),
                Instant.now(),
                TransactionType.CREDIT,
                TransactionStatus.COMPLETED
        );
        Page<Transaction> txPage = new PageImpl<>(List.of(tx));

        // mock transactions
        when(transactionRepository.findByAccountId(accountId, pageable)).thenReturn(txPage);

        // actual function call
        Page<TransactionResult> resultPage = transactionHistoryService.getTransactionHistory(accountId, pageable);

        // verify results
        verify(transactionRepository).findByAccountId(accountId, pageable);
        assertThat(resultPage.getTotalElements()).isEqualTo(1);
        TransactionResult result = resultPage.getContent().getFirst();

        assertThat(result.transactionId()).isEqualTo(tx.getTransactionId());
        assertThat(result.sourceAccountId()).isEqualTo(tx.getSourceAccountId());
        assertThat(result.targetAccountId()).isEqualTo(tx.getTargetAccountId());
        assertThat(result.amount()).isEqualTo(tx.getAmount());
        assertThat(result.type()).isEqualTo(TransactionType.CREDIT);
        assertThat(result.status()).isEqualTo(TransactionStatus.COMPLETED);
        assertThat(result.createdAt()).isEqualTo(tx.getCreatedAt());
    }

    @Test
    void shouldGetEmptyTransactionHistory() {
        // create method argument
        AccountId accountId = AccountId.newId();
        Pageable pageable = PageRequest.of(0,10);

        // create empty transaction list
        Page<Transaction> txPage = new PageImpl<>(List.of());

        // mock db call
        when(transactionRepository.findByAccountId(accountId, pageable)).thenReturn(txPage);

        // actual function call
        Page<TransactionResult> resultPage = transactionHistoryService.getTransactionHistory(accountId, pageable);

        // verify results
        verify(transactionRepository).findByAccountId(accountId, pageable);
        assertTrue(resultPage.isEmpty());
        assertThat(resultPage.getTotalElements()).isEqualTo(0);
    }
}
