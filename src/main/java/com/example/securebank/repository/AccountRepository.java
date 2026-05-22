package com.example.securebank.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.securebank.entity.Account;
import com.example.securebank.entity.User;

public interface AccountRepository extends JpaRepository<Account, Long> {
	
	List<Account> findByUser(User user);

    Optional<Account> findByAccountNumber(String accountNumber);
    


}