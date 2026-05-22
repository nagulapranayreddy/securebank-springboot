package com.example.securebank.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Account {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String accountNumber;
	private BigDecimal balance;
	private LocalDateTime createdAt;
	
	@Enumerated(EnumType.STRING)
	private AccountType accountType;
	
	
	@Enumerated(EnumType.STRING)
	private AccountStatus accountStatus;
	
	
	@ManyToOne
	private User user;

	
	public Account() {
		super();
		
	}

	public Account(Long id, String accountNumber, BigDecimal balance, LocalDateTime createdAt, AccountType accountType,
			AccountStatus accountStatus, User user) {
		super();
		this.id = id;
		this.accountNumber = accountNumber;
		this.balance = balance;
		this.createdAt = createdAt;
		this.accountType = accountType;
		this.accountStatus = accountStatus;
		this.user = user;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	

}
