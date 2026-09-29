package com.zituhoq.banking_transaction.out.database;

import com.zituhoq.banking_transaction.out.database.entities.TransactionJpaEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepo extends JpaRepository<TransactionJpaEntity, Long> {
    Page<TransactionJpaEntity> findByAccountId(Long id, Pageable pageable);
}
