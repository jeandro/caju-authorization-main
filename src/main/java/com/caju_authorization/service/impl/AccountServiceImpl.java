package com.caju_authorization.service.impl;

import com.caju_authorization.constant.MccEnum;
import com.caju_authorization.constant.ResponseCodeEnum;
import com.caju_authorization.mapper.BalanceMapper;
import com.caju_authorization.mapper.MerchantMapper;
import com.caju_authorization.model.Account;
import com.caju_authorization.model.AuthorizationResponse;
import com.caju_authorization.model.Transaction;
import com.caju_authorization.model.dto.CreateAccountDto;
import com.caju_authorization.repository.AccountRepository;
import com.caju_authorization.repository.TransactionRepository;
import com.caju_authorization.service.AccountService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;

    public AccountServiceImpl(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }

    @Override
    public List<Account> getAllAccounts() {
        return this.accountRepository.findAll();
    }

    @Override
    public Account getAccountById(String accountId) {
        return this.accountRepository.findByAccountId(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found with id: " + accountId));
    }

    @Transactional
    @Override
    public AuthorizationResponse authorize(Account account, Transaction transaction) {
        // Ensuring /authorize idempotency
        UUID idempotencyKey = transaction.getIdempotencyKey();
        if (idempotencyKey == null || transactionRepository.findByIdempotencyKey(idempotencyKey).isPresent()) {
            return new AuthorizationResponse(ResponseCodeEnum.ERROR.getCode());
        }

        BigDecimal transactionAmount = transaction.getAmount();

        // Impossible to transact 0 or negative values
        if (transactionAmount.compareTo(BigDecimal.ZERO) <= 0) {
            return new AuthorizationResponse(ResponseCodeEnum.ERROR.getCode());
        }

        // Getting balance type according to merchant name or mcc number
        MccEnum mcc = getBalanceType(transaction.getMerchant(), transaction.getMcc());

        Function<Account, BigDecimal> getter = BalanceMapper.getGetter(mcc);
        BiConsumer<Account, BigDecimal> setter = BalanceMapper.getSetter(mcc);

        BigDecimal balance = getter.apply(account);

        // Account have enough balance to transact
        if (canDeductFromBalance(account, setter, balance, transactionAmount)) {
            accountRepository.save(account);
            transactionRepository.save(transaction);
            return new AuthorizationResponse(ResponseCodeEnum.APPROVED.getCode());
        }

        // Fall backing to cash balance
        BigDecimal cashBalance = account.getCashBalance();
        if (canDeductFromBalance(account, Account::setCashBalance, cashBalance, transactionAmount)) {
            accountRepository.save(account);
            transactionRepository.save(transaction);
            return new AuthorizationResponse(ResponseCodeEnum.APPROVED.getCode());
        }

        // If can´t deduct from correct balance, neither the cash balance, responds with insufficient funds
        return new AuthorizationResponse(ResponseCodeEnum.INSUFFICIENT_FUNDS.getCode());
    }

    @Override
    public void createAccount(CreateAccountDto dto) {
        if (ObjectUtils.isEmpty(dto)) {
            throw new IllegalArgumentException("Account is empty");
        }

        Account account = Account.builder()
                .accountId(dto.getAccountId())
                .cashBalance(dto.getCashBalance())
                .foodBalance(dto.getFoodBalance())
                .mealBalance(dto.getMealBalance())
                .build();

        accountRepository.save(account);
    }

    private MccEnum getBalanceType(String merchant, String mcc) {
        // Gets the correct balance type from known merchants list
        MccEnum mccType = MerchantMapper.getBalanceType(merchant);

        // If the merchant isn´t in the list, defaults to transaction payload mcc
        if (mccType == MccEnum.DEFAULT) {
            mccType = MccEnum.fromMcc(mcc);
        }

        return mccType;
    }

    private boolean canDeductFromBalance(Account account, BiConsumer<Account, BigDecimal> setter, BigDecimal balance, BigDecimal amount) {
        // Re-utilizing logic to compare balances with transaction amount
        if (balance.compareTo(amount) >= 0) {
            setter.accept(account, balance.subtract(amount));
            return true;
        }

        return false;
    }
}
