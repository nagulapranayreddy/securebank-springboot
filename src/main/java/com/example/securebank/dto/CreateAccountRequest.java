package com.example.securebank.dto;

import com.example.securebank.entity.AccountType;

public class CreateAccountRequest {

    private AccountType accountType;

    public CreateAccountRequest() {
    }

    public CreateAccountRequest(AccountType accountType) {
        this.accountType = accountType;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}