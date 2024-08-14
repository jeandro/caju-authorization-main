package com.caju_authorization.service;

import com.caju_authorization.model.Account;
import com.caju_authorization.repository.AccountRepository;
import com.caju_authorization.repository.TransactionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class DataLoader {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public DataLoader(TransactionRepository transactionRepository, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @PostConstruct
    public void init() {
        if (accountRepository.findAll().isEmpty()) {
            Account account1 = new Account();
            account1.setAccountId("1");
            account1.setFoodBalance(new BigDecimal("10.00"));
            account1.setMealBalance(new BigDecimal("10.00"));
            account1.setCashBalance(new BigDecimal("10.00"));
            accountRepository.save(account1);

            Account account2 = new Account();
            account2.setAccountId("2");
            account2.setFoodBalance(new BigDecimal("20.00"));
            account2.setMealBalance(new BigDecimal("20.00"));
            account2.setCashBalance(new BigDecimal("20.00"));
            accountRepository.save(account2);
        }
    }
}
