package com.bhaumik18.finguard.account.repository;

import com.bhaumik18.finguard.account.entity.Account;
import com.bhaumik18.finguard.user.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    
    Optional<Account> findByAccountNumber(String accountNumber);
    
    List<Account> findAllByUser(User user);
    
}