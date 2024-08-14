package com.caju_authorization.service;

import com.caju_authorization.model.Account;
import com.caju_authorization.model.AuthorizationResponse;
import com.caju_authorization.model.Transaction;
import com.caju_authorization.model.dto.CreateAccountDto;

import java.util.List;

public interface AccountService {
    List<Account> getAllAccounts();

    Account getAccountById(String accountId);

    AuthorizationResponse authorize(Account account, Transaction transaction);

    void createAccount (CreateAccountDto dto);
}
