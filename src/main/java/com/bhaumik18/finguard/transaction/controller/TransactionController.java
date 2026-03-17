package com.bhaumik18.finguard.transaction.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bhaumik18.finguard.transaction.dto.TransactionRequest;
import com.bhaumik18.finguard.transaction.dto.TransactionResponse;
import com.bhaumik18.finguard.transaction.service.TransactionService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    /**
     * Endpoint to initiate a new financial transaction.
     * @param request The transaction payload validated against DTO constraints
     * @return 201 Created with the transaction response
     */
    @PostMapping
    public ResponseEntity<TransactionResponse> createTransaction(@Valid @RequestBody TransactionRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        log.info("Received request to create transaction for amount: {}", request.amount());
        
        TransactionResponse response = transactionService.processTransaction(request, userDetails.getUsername());
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    /**
     * SECURED: Endpoint to fetch the transaction history for the authenticated user.
     * Extracts identity from JWT to prevent BOLA/IDOR vulnerabilities.
     */
    @GetMapping
    public ResponseEntity<Page<TransactionResponse>> getTransactions(
            @AuthenticationPrincipal UserDetails userDetails,
            // Added DESC direction so newest transactions appear at the top of your UI
            @PageableDefault(size = 10, sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        
        // 1. Extract email from the cryptographically verified JWT
        String authenticatedEmail = userDetails.getUsername();
        
        // 2. Pass the secure email to the service layer (ignoring client parameters completely)
        Page<TransactionResponse> transactions = transactionService.getTransactions(authenticatedEmail, pageable);
        
        return ResponseEntity.ok(transactions);
    }
}