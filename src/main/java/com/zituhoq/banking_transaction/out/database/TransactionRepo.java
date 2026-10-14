package com.zituhoq.banking_transaction.out.database;

import com.zituhoq.banking_transaction.out.database.entities.TransactionJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.UUID;


public interface TransactionRepo extends JpaRepository<TransactionJpaEntity, UUID> {
    @Query("SELECT t FROM TransactionJpaEntity t WHERE t.sourceAccountId = :accountId OR t.targetAccountId = :accountId")
    Page<TransactionJpaEntity> findByAccountId(@Param("accountId") UUID id, Pageable pageable);
}
