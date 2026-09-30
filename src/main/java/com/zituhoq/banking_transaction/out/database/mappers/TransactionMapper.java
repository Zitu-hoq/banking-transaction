package com.zituhoq.banking_transaction.out.database.mappers;

import com.zituhoq.banking_transaction.core.domain.Transaction;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.TransactionId;
import com.zituhoq.banking_transaction.out.database.entities.TransactionJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    // domain to Jpa
    @Mapping(target = "amount", source = "amount.amount")
    @Mapping(target = "currency", source = "amount.currency")
    @Mapping(target = "transactionId", source = "transactionId.id")
    @Mapping(target = "sourceAccountId", source = "sourceAccountId.id")
    @Mapping(target = "targetAccountId", source = "targetAccountId.id")
    @Mapping(target = "type", source = "type")
    @Mapping(target = "status", source = "status")
    TransactionJpaEntity toTransactionJpaEntity(Transaction transaction);

    // Jpa to domain
    @Mapping(target = "amount", expression = "java(new Money(entity.getAmount(), entity.getCurrency()))")
    @Mapping(target = "transactionId", expression = "java(new TransactionId(entity.getTransactionId()))")
    @Mapping(target = "sourceAccountId", expression = "java(new AccountId(entity.getSourceAccountId()))")
    @Mapping(target = "targetAccountId", expression = "java(new AccountId(entity.getTargetAccountId()))")
    Transaction toTransactionDomain(TransactionJpaEntity entity);
}
