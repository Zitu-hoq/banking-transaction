package com.zituhoq.banking_transaction.out.database.entities;

import com.zituhoq.banking_transaction.core.domain.AccountStatus;
import com.zituhoq.banking_transaction.core.valueObjects.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "accounts")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class AccountJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountId;

    @NotNull
    @Column(unique = true, nullable = false)
    private Long ownerId;

    @NotNull
    @DecimalMin("0.00")
    @Column(nullable = false, precision = 20, scale = 2)
    @Setter
    private BigDecimal balance;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private Currency currency;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Setter
    private AccountStatus status;

    @Version
    Long version;

}
