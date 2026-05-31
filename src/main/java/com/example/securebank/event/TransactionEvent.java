package com.example.securebank.event;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionEvent {

    private String accountNumber;
    private String transactionType;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;

    public TransactionEvent() {
    }

    public TransactionEvent(String accountNumber, String transactionType,
                            BigDecimal amount, String description,
                            LocalDateTime createdAt) {
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}