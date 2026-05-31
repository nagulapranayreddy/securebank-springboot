package com.example.securebank.controller;

import org.springframework.http.ResponseEntity;
import com.example.securebank.dto.DepositRequest;
import com.example.securebank.dto.TransferRequest;
import com.example.securebank.dto.WithdrawRequest;

import org.springframework.web.bind.annotation.*;

import com.example.securebank.dto.CreateAccountRequest;
import com.example.securebank.service.AccountService;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/create")
    public ResponseEntity<String> createAccount(@Valid @RequestBody CreateAccountRequest request) {
        String response = accountService.createAccount(request);
        return ResponseEntity.ok(response);
    }
    
    
    
    @GetMapping("/my")
    public ResponseEntity<?> getMyAccounts() {
        return ResponseEntity.ok(accountService.getMyAccounts());
    }
    
    
    
    @GetMapping("/{accountNumber}")
    public ResponseEntity<?> getAccountByAccountNumber(
            @PathVariable String accountNumber) {

        return ResponseEntity.ok(
                accountService.getAccountByAccountNumber(accountNumber)
        );
    }
    
    
    @PostMapping("/deposit")
    public ResponseEntity<String> deposit(
    		@Valid @RequestBody DepositRequest request
    ) {
    	
    	//System.out.println("DEPOSIT CONTROLLER CALLED");

        String response = accountService.deposit(request);

        return ResponseEntity.ok(response);
    }
    
    
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(
    		@Valid @RequestBody WithdrawRequest request
    ) {

        String response = accountService.withdraw(request);

        return ResponseEntity.ok(response);
    }
    
    @Operation(summary = "Transfer money between accounts")
    @PostMapping("/transfer")
    public ResponseEntity<String> transfer(
    		@Valid @RequestBody TransferRequest request
    ) {

        String response = accountService.transfer(request);

        return ResponseEntity.ok(response);
    }
    
    
    
}