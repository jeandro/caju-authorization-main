package com.caju_authorization.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "transaction")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", referencedColumnName = "account_id", nullable = false)
    private Account account;

    @Column(name = "amount", nullable = false)
    private BigDecimal amount;

    @Column(name = "mcc", nullable = false)
    private String mcc;

    @Column(name = "merchant", nullable = false)
    private String merchant;

    @Column(name = "idempotency_key", unique = true, nullable = false)
    private UUID idempotencyKey;

    public Transaction(Account account, BigDecimal amount, String mcc, String merchant, UUID idempotencyKey) {
        this.account = account;
        this.amount = amount;
        this.mcc = mcc;
        this.merchant = merchant;
        this.idempotencyKey = idempotencyKey;
    }

}
