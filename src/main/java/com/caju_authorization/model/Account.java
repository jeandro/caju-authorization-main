package com.caju_authorization.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Table(name = "account")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "account_id", nullable = false, unique = true)
    private String accountId;

    @Column(name = "food_balance", nullable = false)
    private BigDecimal foodBalance;

    @Column(name = "meal_balance", nullable = false)
    private BigDecimal mealBalance;

    @Column(name = "cash_balance", nullable = false)
    private BigDecimal cashBalance;

}
