package com.example.securebank.service;

import java.math.BigDecimal;
import com.example.securebank.event.TransactionEvent;

import com.example.securebank.entity.Transaction;
import com.example.securebank.entity.TransactionStatus;
import com.example.securebank.entity.TransactionType;
import com.example.securebank.repository.TransactionRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.securebank.dto.CreateAccountRequest;
import com.example.securebank.dto.DepositRequest;
import com.example.securebank.dto.TransferRequest;
import com.example.securebank.dto.WithdrawRequest;
import com.example.securebank.entity.Account;
import com.example.securebank.entity.AccountStatus;
import com.example.securebank.entity.User;
import com.example.securebank.repository.AccountRepository;
import com.example.securebank.repository.UserRepository;

import jakarta.transaction.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import com.example.securebank.dto.AccountResponse;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final KafkaProducerService kafkaProducerService;
    
    

    public AccountService(
            AccountRepository accountRepository,
            UserRepository userRepository,
            TransactionRepository transactionRepository,
            KafkaProducerService kafkaProducerService
    ) {
        this.accountRepository = accountRepository;
        this.userRepository = userRepository;
        this.transactionRepository = transactionRepository;
        this.kafkaProducerService = kafkaProducerService;
    }
    
    
    
    @CacheEvict(value = "myAccounts", allEntries = true)
    public String createAccount(CreateAccountRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = new Account();
        account.setAccountType(request.getAccountType());
        account.setAccountStatus(AccountStatus.ACTIVE);
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(LocalDateTime.now());
        account.setAccountNumber(generateAccountNumber());
        account.setUser(user);

        accountRepository.save(account);

        return "Account created successfully";
    }

    private String generateAccountNumber() {
        return "SB" + (1000000000L + new Random().nextInt(900000000));
    }
    
    
    @Cacheable(
    	    value = "myAccounts",
    	    key = "T(org.springframework.security.core.context.SecurityContextHolder).getContext().getAuthentication().getName()"
    	)
    	public List<AccountResponse> getMyAccounts() {

    	    String email = SecurityContextHolder.getContext()
    	            .getAuthentication()
    	            .getName();

    	    User user = userRepository.findByEmail(email)
    	            .orElseThrow(() -> new RuntimeException("User not found"));

    	    
    	    System.out.println("Fetching my accounts from DATABASE...");
    	    List<Account> accounts = accountRepository.findByUser(user);

    	    return accounts.stream()
    	            .map(this::mapToResponse)
    	            .collect(Collectors.toList());
    	}
    
    
    
    
    
    
    
    public AccountResponse getAccountByAccountNumber(String accountNumber) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByAccountNumber(accountNumber)
        		.orElseThrow(() -> new RuntimeException("Receiver account number is invalid. Please check and try again."));
        
        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        return mapToResponse(account);
    }
    
    
    private AccountResponse mapToResponse(Account account) {

        return new AccountResponse(
                account.getAccountType(),
                account.getAccountStatus(),
                account.getAccountNumber(),
                account.getBalance(),
                account.getCreatedAt()
        );
    }
    
    
    
    @CacheEvict(value = "myAccounts", allEntries = true)
    public String deposit(DepositRequest request) {

    	
    	// System.out.println("DEPOSIT METHOD STARTED");
    	 
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository
                .findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // SECURITY CHECK
        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        // VALIDATION
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount should be greater than zero");
        }

        // UPDATE BALANCE
        account.setBalance(
                account.getBalance().add(request.getAmount())
        );
        
        LocalDateTime now = LocalDateTime.now();
        
        Transaction transaction = new Transaction();


        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(now);
        transaction.setDescription("Money deposited");
        transaction.setTransactionType(TransactionType.DEPOSIT);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);
        
        transactionRepository.save(transaction);

        TransactionEvent event = new TransactionEvent(
                account.getAccountNumber(),
                "DEPOSIT",
                request.getAmount(),
                "Money deposited",
                now
        );

       // System.out.println("Deposit completed. Calling Kafka producer...");
        kafkaProducerService.sendTransactionEvent(event);

        return "Amount deposited successfully";

    }
    
    
    
    @CacheEvict(value = "myAccounts", allEntries = true)
    public String withdraw(WithdrawRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new RuntimeException("Account not found"));

        if (!account.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount should be greater than zero");
        }

        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            return "Insufficient balance";
        }

        account.setBalance(account.getBalance().subtract(request.getAmount()));

        accountRepository.save(account);

        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setAmount(request.getAmount());
        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setDescription("Money withdrawn");
        transaction.setTransactionType(TransactionType.WITHDRAW);
        transaction.setTransactionStatus(TransactionStatus.SUCCESS);

        transactionRepository.save(transaction);
        
        TransactionEvent event = new TransactionEvent(
                account.getAccountNumber(),
                "WITHDRAW",
                request.getAmount(),
                "Money withdrawn",
                LocalDateTime.now()
        );

        kafkaProducerService.sendTransactionEvent(event);

        return "Amount withdrawn successfully";
    }
    
    
    @CacheEvict(value = "myAccounts", allEntries = true)
    @Transactional
    public String transfer(TransferRequest request) {

        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new RuntimeException("Sender account number is invalid. Please check and try again."));

        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new RuntimeException("Receiver account number is invalid. Please check and try again."));

        if (!fromAccount.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Access denied. You can transfer only from your own account.");
        }

        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Amount should be greater than zero");
        }

        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new RuntimeException("Insufficient balance");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(request.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(request.getAmount()));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        Transaction debitTransaction = new Transaction();
        debitTransaction.setAccount(fromAccount);
        debitTransaction.setAmount(request.getAmount());
        debitTransaction.setCreatedAt(LocalDateTime.now());
        debitTransaction.setDescription("Money transferred to " + toAccount.getAccountNumber());
        debitTransaction.setTransactionType(TransactionType.TRANSFER);
        debitTransaction.setTransactionStatus(TransactionStatus.SUCCESS);

        Transaction creditTransaction = new Transaction();
        creditTransaction.setAccount(toAccount);
        creditTransaction.setAmount(request.getAmount());
        creditTransaction.setCreatedAt(LocalDateTime.now());
        creditTransaction.setDescription("Money received from " + fromAccount.getAccountNumber());
        creditTransaction.setTransactionType(TransactionType.TRANSFER);
        creditTransaction.setTransactionStatus(TransactionStatus.SUCCESS);

        transactionRepository.save(debitTransaction);
        transactionRepository.save(creditTransaction);
        
        
        TransactionEvent debitEvent = new TransactionEvent(
                fromAccount.getAccountNumber(),
                "TRANSFER",
                request.getAmount(),
                "Money transferred to " + toAccount.getAccountNumber(),
                LocalDateTime.now()
        );

        TransactionEvent creditEvent = new TransactionEvent(
                toAccount.getAccountNumber(),
                "TRANSFER",
                request.getAmount(),
                "Money received from " + fromAccount.getAccountNumber(),
                LocalDateTime.now()
        );

        kafkaProducerService.sendTransactionEvent(debitEvent);
        kafkaProducerService.sendTransactionEvent(creditEvent);

        return "Amount transferred successfully";
    }
    
}