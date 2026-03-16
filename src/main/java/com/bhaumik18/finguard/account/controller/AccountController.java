package com.bhaumik18.finguard.account.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.bhaumik18.finguard.account.entity.Account;
import com.bhaumik18.finguard.account.service.AccountService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    public ResponseEntity<List<Account>> getUserAccounts(Authentication authentication) {
        // authentication.getName() automatically extracts the email from the validated JWT
        String userEmail = authentication.getName(); 
        List<Account> accounts = accountService.getMyAccounts(userEmail);
        
        return ResponseEntity.ok(accounts);
    }
}