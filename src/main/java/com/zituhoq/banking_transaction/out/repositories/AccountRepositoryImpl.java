package com.zituhoq.banking_transaction.out.repositories;

import com.zituhoq.banking_transaction.core.domain.Account;
import com.zituhoq.banking_transaction.core.ports.out.AccountRepository;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;
import com.zituhoq.banking_transaction.out.database.AccountRepo;
import com.zituhoq.banking_transaction.out.database.entities.AccountJpaEntity;
import com.zituhoq.banking_transaction.out.database.mappers.AccountMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepositoryImpl implements AccountRepository {
    private final AccountRepo accountRepo;
    private final AccountMapper mapper;
    public AccountRepositoryImpl(AccountRepo accountRepo, AccountMapper mapper) {
        this.accountRepo = accountRepo;
        this.mapper = mapper;
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        return accountRepo.findById(id.id()).map(mapper::toAccountDomain);
    }

    @Override
    public boolean existsById(AccountId id) {
        return accountRepo.existsById(id.id());
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = mapper.toAccountJpaEntity(account);
        AccountJpaEntity savedEntity = accountRepo.save(entity);
        return mapper.toAccountDomain(savedEntity);
    }
}
