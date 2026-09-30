package com.zituhoq.banking_transaction.out.database.mappers;

import com.zituhoq.banking_transaction.core.domain.Account;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.core.valueObjects.OwnerId;
import com.zituhoq.banking_transaction.out.database.entities.AccountJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // domain to JPA
    @Mapping(target = "balance", source = "balance.amount")
    @Mapping(target = "currency", source = "balance.currency")
    @Mapping(target = "ownerId", source = "ownerId.id")
    @Mapping(target = "accountId", source = "accountId.id")
    AccountJpaEntity toAccountJpaEntity(Account account);

    //JPA to domain
    @Mapping(target = "balance", expression = "java(new Money(entity.getBalance(), entity.getCurrency()))")
    @Mapping(target = "accountId", expression = "java(new AccountId(entity.getAccountId()))")
    @Mapping(target = "ownerId", expression = "java(new OwnerId(entity.getOwnerId()))")
    Account toAccountDomain(AccountJpaEntity entity);

}
