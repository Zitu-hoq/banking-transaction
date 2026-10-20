package com.zituhoq.banking_transaction.in.web.mapper;

import com.zituhoq.banking_transaction.core.services.dto.DepositCommand;
import com.zituhoq.banking_transaction.core.services.dto.TransactionResult;
import com.zituhoq.banking_transaction.core.services.dto.TransferCommand;
import com.zituhoq.banking_transaction.core.services.dto.WithdrawCommand;
import com.zituhoq.banking_transaction.in.web.dto.DepositRequest;
import com.zituhoq.banking_transaction.in.web.dto.TransactionResponse;
import com.zituhoq.banking_transaction.in.web.dto.TransferRequest;
import com.zituhoq.banking_transaction.in.web.dto.WithdrawRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionDtoMapper {
    // DTO -> Command
    @Mapping(target = "amount", expression = "java(new Money(request.amount(), request.currency()))")
    WithdrawCommand toWithdrawCommand(WithdrawRequest request);

    @Mapping(target = "amount", expression = "java(new Money(request.amount(), request.currency()))")
    DepositCommand toDepositCommand(DepositRequest request);

    @Mapping(target = "amount", expression = "java(new Money(request.amount(), request.currency()))")
    TransferCommand toTransferCommand(TransferRequest request);

    @Mapping(target = "amount", expression = "java(new Money(response.amount(), response.currency()))")
    TransactionResult toTransactionResult(TransactionResponse response);

    // Command -> DTO
    @Mapping(target = "amount", source = "amount.amount")
    @Mapping(target = "currency", source = "amount.currency")
    WithdrawRequest toWithdrawRequest(WithdrawCommand command);

    @Mapping(target = "amount", source = "amount.amount")
    @Mapping(target = "currency", source = "amount.currency")
    DepositRequest toDepositRequest(DepositCommand command);

    @Mapping(target = "amount", source = "amount.amount")
    @Mapping(target = "currency", source = "amount.currency")
    TransferRequest toTransferRequest(TransferCommand command);

    @Mapping(target = "amount", source = "amount.amount")
    @Mapping(target = "currency", source = "amount.currency")
    TransactionResponse toTransactionResponse(TransactionResult result);

}
