package com.zituhoq.banking_transaction.core.ports.out;

import com.zituhoq.banking_transaction.core.domain.Account;
import com.zituhoq.banking_transaction.core.valueObjects.AccountId;

import java.util.Optional;

public interface AccountRepository {
    Optional<Account> findById(AccountId id);
    boolean existsById(AccountId id);
    Account save(Account account);
}
