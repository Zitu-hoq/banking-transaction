package com.zituhoq.banking_transaction.out.database;

import com.zituhoq.banking_transaction.out.database.entities.AccountJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AccountRepo extends JpaRepository<AccountJpaEntity, UUID> {
}
