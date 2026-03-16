package com.bhaumik18.finguard.account.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.bhaumik18.finguard.account.entity.Account;
import com.bhaumik18.finguard.account.repository.AccountRepository;
import com.bhaumik18.finguard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    
    public List<Account> getMyAccounts(String email){
    	var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
                
            // Use the new repository method we just created!
            return accountRepository.findAllByUser(user); 
    }

    
}