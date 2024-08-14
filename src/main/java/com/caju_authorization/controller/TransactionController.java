package com.caju_authorization.controller;

import com.caju_authorization.model.Transaction;
import com.caju_authorization.model.dto.CreateTransactionDto;
import com.caju_authorization.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<List<Transaction>> getAllTransactions() {
        List<Transaction> allTransactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(allTransactions);
    }

    @PostMapping("/create")
    public ResponseEntity<CreateTransactionDto> createTransaction(CreateTransactionDto dto) {
        transactionService.createTransaction(dto);
        return ResponseEntity.ok(dto);
    }
}
