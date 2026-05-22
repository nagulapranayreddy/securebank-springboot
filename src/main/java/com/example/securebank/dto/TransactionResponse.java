package com.example.securebank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.securebank.entity.TransactionStatus;
import com.example.securebank.entity.TransactionType;

public class TransactionResponse {

    private TransactionType transactionType;
    private TransactionStatus transactionStatus;
    private BigDecimal amount;
    private String description;
    private LocalDateTime createdAt;

    public TransactionResponse() {
    }

    public TransactionResponse(TransactionType transactionType, TransactionStatus transactionStatus,
                               BigDecimal amount, String description, LocalDateTime createdAt) {
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.amount = amount;
        this.description = description;
        this.createdAt = createdAt;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public TransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(TransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
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