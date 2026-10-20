package com.zituhoq.banking_transaction.in.web.mapper;

import com.zituhoq.banking_transaction.core.domain.TransactionStatus;
import com.zituhoq.banking_transaction.core.domain.TransactionType;
import com.zituhoq.banking_transaction.core.services.dto.DepositCommand;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.services.dto.TransferCommand;
import com.zituhoq.banking_transaction.core.services.dto.WithdrawCommand;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;
import com.zituhoq.banking_transaction.in.web.dto.DepositRequest;
import com.zituhoq.banking_transaction.in.web.dto.TransactionResponse;
import com.zituhoq.banking_transaction.in.web.dto.TransferRequest;
import com.zituhoq.banking_transaction.in.web.dto.WithdrawRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TransactionDtoMapperTest {

    private final TransactionDtoMapper transactionDtoMapper = new TransactionDtoMapperImpl();

    // common variables
    String key;
    String xid;
    UUID id;
    Currency currency;
    BigDecimal amount;
    Money money;

    @BeforeEach
    void setUp() {
        key = "1";
        xid = "x-12";
        id = UUID.randomUUID();
        currency = Currency.BDT;
        amount = BigDecimal.TEN;
        money = new Money(amount, currency);
    }

    @Test
    void shouldMapWithdrawCommand(){
        // input
        WithdrawRequest request = new WithdrawRequest(id, amount, currency, key, xid);

        // expect
        WithdrawCommand cmd = new WithdrawCommand(new AccountId(id), money, key, xid);

        // function call
        WithdrawCommand res = transactionDtoMapper.toWithdrawCommand(request);

        // check
        assertEquals(id, res.accountId().id());
        assertEquals(key, res.idempotencyKey());
        assertEquals(xid, res.correlationId());
        assertEquals(amount, res.amount().amount());
        assertEquals(currency, res.amount().currency());
    }

    @Test
    void shouldMapDepositCommand() {
        // input
        DepositRequest request = new DepositRequest(id, amount, currency, key, xid);

        //function call
        DepositCommand res = transactionDtoMapper.toDepositCommand(request);

        // check
        assertEquals(id, res.accountId().id());
        assertEquals(amount, res.amount().amount());
        assertEquals(currency, res.amount().currency());
        assertEquals(key, res.idempotencyKey());
        assertEquals(xid, res.correlationId());
    }

    @Test
    void shouldMapTransferCommand() {
        TransferRequest request = new TransferRequest(id, UUID.randomUUID(), amount, currency, key, xid);

        TransferCommand res = transactionDtoMapper.toTransferCommand(request);

        assertEquals(id, res.sourceAccountId().id());
        assertEquals(amount, res.amount().amount());
        assertEquals(currency, res.amount().currency());
        assertEquals(key, res.idempotencyKey());
        assertEquals(xid, res.correlationId());
    }

    @Test
    void shouldMapTransactionResult() {
        UUID transactionId = UUID.randomUUID();
        Instant creationTime = Instant.now();
        UUID targetAccountId = UUID.randomUUID();
        TransactionResponse response = new TransactionResponse(transactionId, id, targetAccountId, amount, currency, TransactionType.TRANSFER, TransactionStatus.COMPLETED, creationTime);

        // function call
        TransactionResult res = transactionDtoMapper.toTransactionResult(response);

        assertEquals(transactionId, res.transactionId().id());
        assertEquals(id, res.sourceAccountId().id());
        assertEquals(targetAccountId, res.targetAccountId().id());
        assertEquals(amount, res.amount().amount());
        assertEquals(currency, res.amount().currency());
        assertEquals(TransactionType.TRANSFER, res.type());
        assertEquals(TransactionStatus.COMPLETED, res.status());
        assertEquals(creationTime, res.createdAt());
    }


    @Test
    void shouldMapWithdrawRequestFromCommand() {
        WithdrawCommand cmd = new WithdrawCommand(new AccountId(id), money, key, xid);

        WithdrawRequest res = transactionDtoMapper.toWithdrawRequest(cmd);

        assertEquals(id, res.accountId());
        assertEquals(amount, res.amount());
        assertEquals(currency, res.currency());
        assertEquals(key, res.idempotencyKey());
        assertEquals(xid, res.correlationId());
    }


    @Test
    void shouldMapDepositRequestFromCommand() {
        DepositCommand cmd = new DepositCommand(new AccountId(id), money, key, xid);

        DepositRequest res = transactionDtoMapper.toDepositRequest(cmd);

        assertEquals(id, res.accountId());
        assertEquals(amount, res.amount());
        assertEquals(currency, res.currency());
        assertEquals(key, res.idempotencyKey());
        assertEquals(xid, res.correlationId());
    }

    @Test
    void shouldMapTransferRequestFromCommand() {
        UUID targetId = UUID.randomUUID();
        TransferCommand cmd = new TransferCommand(new AccountId(id), new AccountId(targetId), money, key, xid);

        TransferRequest res = transactionDtoMapper.toTransferRequest(cmd);

        assertEquals(id, res.sourceAccountId());
        assertEquals(targetId, res.targetAccountId());
        assertEquals(amount, res.amount());
        assertEquals(currency, res.currency());
        assertEquals(key, res.idempotencyKey());
        assertEquals(xid, res.correlationId());
    }

    @Test
    void shouldMapTransactionResponseFromResult() {
        UUID transactionId = UUID.randomUUID();
        Instant creationTime = Instant.now();
        UUID targetAccountId = UUID.randomUUID();
        TransactionResult result = new TransactionResult(
                new TransactionId(transactionId), new AccountId(id), new AccountId(targetAccountId), money,
                TransactionType.TRANSFER, TransactionStatus.COMPLETED, creationTime
        );

        TransactionResponse res = transactionDtoMapper.toTransactionResponse(result);

        assertEquals(transactionId, res.transactionId());
        assertEquals(id, res.sourceAccountId());
        assertEquals(targetAccountId, res.targetAccountId());
        assertEquals(amount, res.amount());
        assertEquals(currency, res.currency());
        assertEquals(TransactionType.TRANSFER, res.type());
        assertEquals(TransactionStatus.COMPLETED, res.status());
        assertEquals(creationTime, res.createdAt());
    }

}
