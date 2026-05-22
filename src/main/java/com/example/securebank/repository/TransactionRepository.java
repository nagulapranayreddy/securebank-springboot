package com.example.securebank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securebank.entity.Account;
import com.example.securebank.entity.Transaction;

public interface TransactionRepository
        extends JpaRepository<Transaction, Long> {

    List<Transaction> findByAccount(Account account);
}