package com.example.securebank.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.securebank.dto.TransactionResponse;
import com.example.securebank.entity.Account;
import com.example.securebank.entity.Transaction;
import com.example.securebank.entity.User;
import com.example.securebank.repository.AccountRepository;
import com.example.securebank.repository.TransactionRepository;
import com.example.securebank.repository.UserRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository,
            UserRepository userRepository
    ) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
    }

    public List<TransactionResponse> getTransactionsByAccount(
            String accountNumber
    ) {

        String email = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository
                .findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // SECURITY CHECK
        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        List<Transaction> transactions =
                transactionRepository.findByAccount(account);

        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(
            Transaction transaction
    ) {

        return new TransactionResponse(
                transaction.getTransactionType(),
                transaction.getTransactionStatus(),
                transaction.getAmount(),
                transaction.getDescription(),
                transaction.getCreatedAt()
        );
    }
}