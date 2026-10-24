package com.zituhoq.banking_transaction.in.web.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zituhoq.banking_transaction.core.domain.exception.AccountNotFoundException;
import com.zituhoq.banking_transaction.core.ports.in.DepositUseCase;
import com.zituhoq.banking_transaction.core.ports.in.GetTransactionHistoryUseCase;
import com.zituhoq.banking_transaction.core.ports.in.TransferMoneyUseCase;
import com.zituhoq.banking_transaction.core.ports.in.WithdrawUseCase;
import com.zituhoq.banking_transaction.core.services.dto.DepositCommand;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.services.dto.TransferCommand;
import com.zituhoq.banking_transaction.core.services.dto.WithdrawCommand;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import com.zituhoq.banking_transaction.in.web.dto.DepositRequest;
import com.zituhoq.banking_transaction.in.web.dto.TransactionResponse;
import com.zituhoq.banking_transaction.in.web.dto.TransferRequest;
import com.zituhoq.banking_transaction.in.web.dto.WithdrawRequest;
import com.zituhoq.banking_transaction.in.web.mapper.TransactionDtoMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AccountControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private DepositUseCase depositUseCase;

    @MockitoBean
    private TransactionDtoMapper transactionDtoMapper;
    @MockitoBean
    private WithdrawUseCase withdrawUseCase;
    @MockitoBean
    private TransferMoneyUseCase transferMoneyUseCase;
    @MockitoBean
    private GetTransactionHistoryUseCase getTransactionHistoryUseCase;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final TransactionResult result = mock(TransactionResult.class);
    private final TransactionResponse response = mock(TransactionResponse.class);


    @Test
    void shouldHandleDepositReq() throws Exception {
        DepositRequest req = new DepositRequest(UUID.randomUUID(), BigDecimal.valueOf(10), Currency.BDT, "1", "X-11");

        DepositCommand cmd = mock(DepositCommand.class);

        when(transactionDtoMapper.toDepositCommand(req)).thenReturn(cmd);
        when(depositUseCase.deposit(cmd)).thenReturn(result);
        when(transactionDtoMapper.toTransactionResponse(result)).thenReturn(response);

        mockMvc.perform(
                post("/api/v1/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(depositUseCase).deposit(cmd);
    }

    @Test
    void shouldGiveValidationErr() throws Exception {
        DepositRequest req = new DepositRequest(null, BigDecimal.valueOf(10), Currency.BDT, "1", "x-11");

        mockMvc.perform(
                        post("/api/v1/transactions/deposit")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> {
                    assertInstanceOf(MethodArgumentNotValidException.class, result.getResolvedException());
                });

        verify(transactionDtoMapper, never()).toDepositCommand(req);
        verify(depositUseCase, never()).deposit(any(DepositCommand.class));

    }

    @Test
    void shouldHandleWithdrawReq() throws Exception {
        WithdrawRequest req = new WithdrawRequest(UUID.randomUUID(), BigDecimal.valueOf(10), Currency.BDT, "1", "X-11");

        WithdrawCommand cmd = mock(WithdrawCommand.class);

        when(transactionDtoMapper.toWithdrawCommand(req)).thenReturn(cmd);
        when(withdrawUseCase.withdraw(cmd)).thenReturn(result);
        when(transactionDtoMapper.toTransactionResponse(result)).thenReturn(response);

        mockMvc.perform(post("/api/v1/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(withdrawUseCase).withdraw(cmd);

    }

    @Test
    void shouldHandleTransferMoneyReq() throws Exception {

        TransferRequest req = new TransferRequest(UUID.randomUUID(), UUID.randomUUID(), BigDecimal.valueOf(10), Currency.BDT, "1", "X-11");

        TransferCommand cmd = mock(TransferCommand.class);

        when(transactionDtoMapper.toTransferCommand(req)).thenReturn(cmd);
        when(transferMoneyUseCase.transfer(cmd)).thenReturn(result);
        when(transactionDtoMapper.toTransactionResponse(result)).thenReturn(response);

        mockMvc.perform(post("/api/v1/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk());

        verify(transferMoneyUseCase).transfer(cmd);
    }

    @Test
    void shouldHandleGetTransactionsReq() throws Exception {

        UUID accountId = UUID.randomUUID();

        Page<TransactionResult> results = new PageImpl<>(List.of(result));

        when(getTransactionHistoryUseCase.getTransactionHistory(any(AccountId.class), any(Pageable.class)))
                .thenReturn(results);
        when(transactionDtoMapper.toTransactionResponse(result)).thenReturn(response);

        mockMvc.perform(get("/api/v1/accounts/{accountId}/transactions", accountId)
                        .param("page", "0")
                        .param("size", "10"))
                        .andExpect(status().isOk());

        verify(getTransactionHistoryUseCase).getTransactionHistory(any(AccountId.class),
                any(Pageable.class));

    }

    @Test
    void shouldThrowAccountNotFound() throws Exception {
        UUID sourceAccountId = UUID.randomUUID();
        UUID targetAccountId = UUID.randomUUID();

        TransferRequest req = new TransferRequest(sourceAccountId, targetAccountId, BigDecimal.valueOf(10), Currency.BDT, "1", "X-11");

        TransferCommand command = mock(TransferCommand.class);

        when(transactionDtoMapper.toTransferCommand(req)).thenReturn(command);
        when(transferMoneyUseCase.transfer(any(TransferCommand.class))).thenThrow(new AccountNotFoundException("Source account not found"));


        mockMvc.perform(post("/api/v1/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());

        verify(transferMoneyUseCase).transfer(any(TransferCommand.class));
        verify(transactionDtoMapper, never()).toTransactionResponse(any());

    }
}
