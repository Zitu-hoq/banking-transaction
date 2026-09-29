package com.zituhoq.banking_transaction.out.database.mappers;

import com.zituhoq.banking_transaction.core.domain.Account;
import com.zituhoq.banking_transaction.core.valueObjects.Money;
import com.zituhoq.banking_transaction.out.database.entities.AccountJpaEntity;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring")
public interface AccountMapper {

    // domain to JPA
    @Mapping(target = "balance", source = "balance.amount")
    @Mapping(target = "currency", source = "balance.currency")
    AccountJpaEntity toAccountJpaEntity(Account account);

    //JPA to domain
    @Mapping(target = "balance", expression = "java(new Money(entity.getBalance(), entity.getCurrency()))")
    Account toAccountDomain(AccountJpaEntity entity);

}
