package com.example.securebank.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.example.securebank.entity.AccountStatus;
import com.example.securebank.entity.AccountType;

public class AccountResponse {
	
	private AccountType accountType;
	private AccountStatus accountStatus;
	private String accountNumber;
	private BigDecimal balance;
	private LocalDateTime createdAt;
	
	
	
	public AccountResponse() {
		super();
		
	}

	public AccountResponse(AccountType accountType, AccountStatus accountStatus, String accountNumber,
			BigDecimal balance, LocalDateTime createdAt) {
		super();
		this.accountType = accountType;
		this.accountStatus = accountStatus;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.createdAt = createdAt;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public AccountStatus getAccountStatus() {
		return accountStatus;
	}


	public void setAccountStatus(AccountStatus accountStatus) {
		this.accountStatus = accountStatus;
	}


	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
	
	

}
