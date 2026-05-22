package com.example.securebank.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.securebank.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<?> getTransactionsByAccount(
            @PathVariable String accountNumber
    ) {
        return ResponseEntity.ok(
                transactionService.getTransactionsByAccount(accountNumber)
        );
    }
}