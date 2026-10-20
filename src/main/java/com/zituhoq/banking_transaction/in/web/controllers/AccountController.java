package com.zituhoq.banking_transaction.in.web.controllers;

import com.zituhoq.banking_transaction.core.ports.in.DepositUseCase;
import com.zituhoq.banking_transaction.core.ports.in.GetTransactionHistoryUseCase;
import com.zituhoq.banking_transaction.core.ports.in.TransferMoneyUseCase;
import com.zituhoq.banking_transaction.core.ports.in.WithdrawUseCase;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.in.web.dto.DepositRequest;
import com.zituhoq.banking_transaction.in.web.dto.TransactionResponse;
import com.zituhoq.banking_transaction.in.web.dto.TransferRequest;
import com.zituhoq.banking_transaction.in.web.dto.WithdrawRequest;
import com.zituhoq.banking_transaction.in.web.mapper.TransactionDtoMapper;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
@Validated
public class AccountController {

    private final TransactionDtoMapper mapper;

    private final DepositUseCase depositUseCase;
    private final WithdrawUseCase withdrawUseCase;
    private final TransferMoneyUseCase transferMoneyUseCase;
    private final GetTransactionHistoryUseCase getTransactionHistoryUseCase;

    public AccountController(DepositUseCase depositUseCase, TransactionDtoMapper mapper, WithdrawUseCase withdrawUseCase, TransferMoneyUseCase transferMoneyUseCase, GetTransactionHistoryUseCase getTransactionHistoryUseCase) {
        this.depositUseCase = depositUseCase;
        this.mapper = mapper;
        this.withdrawUseCase = withdrawUseCase;
        this.transferMoneyUseCase = transferMoneyUseCase;
        this.getTransactionHistoryUseCase = getTransactionHistoryUseCase;
    }

    // deposit
    @PostMapping("/transactions/deposit")
    public ResponseEntity<TransactionResponse>  deposit(@Valid @RequestBody DepositRequest depositRequest) {

        TransactionResult result = depositUseCase.deposit(mapper.toDepositCommand(depositRequest));

        return ResponseEntity.ok(mapper.toTransactionResponse(result));
    }

    // withdraw
    @PostMapping("/transactions/withdraw")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawRequest withdrawRequest) {

        TransactionResult result = withdrawUseCase.withdraw(mapper.toWithdrawCommand(withdrawRequest));

        return  ResponseEntity.ok(mapper.toTransactionResponse(result));
    }


    // transfer
    @PostMapping("/transactions/transfer")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest transferRequest) {

        TransactionResult result = transferMoneyUseCase.transfer(mapper.toTransferCommand(transferRequest));

        return ResponseEntity.ok(mapper.toTransactionResponse(result));
    }



    // transactions
    @GetMapping("/accounts/{accountId}/transactions")
    public ResponseEntity<Page<TransactionResponse>> getTransactions(@PathVariable("accountId") UUID accountId, Pageable pageable) {

        AccountId id = new AccountId(accountId);

        Page<TransactionResult> results = getTransactionHistoryUseCase.getTransactionHistory(id, pageable);

        Page<TransactionResponse> response = results.map(mapper::toTransactionResponse);

        return ResponseEntity.ok(response);
    }

}
