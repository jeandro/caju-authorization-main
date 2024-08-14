package com.caju_authorization.controller;

import com.caju_authorization.constant.ResponseCodeEnum;
import com.caju_authorization.model.Account;
import com.caju_authorization.model.AuthorizationResponse;
import com.caju_authorization.model.Transaction;
import com.caju_authorization.model.dto.CreateTransactionDto;
import com.caju_authorization.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping()
public class AuthorizeController {
    private final AccountService accountService;

    public AuthorizeController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/authorize")
    public ResponseEntity<AuthorizationResponse> authorize(@RequestBody CreateTransactionDto transactionDto) {
        try {
            Account account = accountService.getAccountById(transactionDto.getAccountId());

            Transaction transaction = Transaction.builder()
                    .account(account)
                    .amount(transactionDto.getAmount())
                    .mcc(transactionDto.getMcc())
                    .merchant(transactionDto.getMerchant())
                    .idempotencyKey(transactionDto.getIdempotencyKey())
                    .build();

            AuthorizationResponse response = accountService.authorize(account, transaction);

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new AuthorizationResponse(ResponseCodeEnum.ERROR.getCode()), HttpStatus.OK);
        }
    }
}
