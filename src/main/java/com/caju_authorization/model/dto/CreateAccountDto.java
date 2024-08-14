package com.caju_authorization.model.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CreateAccountDto {
    private String accountId;

    private BigDecimal foodBalance;

    private BigDecimal mealBalance;

    private BigDecimal cashBalance;
}
