package com.caju_authorization.model.dto;


import com.caju_authorization.model.Account;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateTransactionDto {

    private Account account;

    private String accountId;

    private BigDecimal amount;

    private String mcc;

    private String merchant;

    private UUID idempotencyKey;
}
