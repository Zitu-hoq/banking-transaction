package com.zituhoq.banking_transaction.out.database;

import com.zituhoq.banking_transaction.out.database.entities.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepo extends JpaRepository<AccountJpaEntity, Long> {
}
