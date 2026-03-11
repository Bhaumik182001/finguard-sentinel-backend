package com.bhaumik18.finguard.transaction.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bhaumik18.finguard.transaction.dto.TransactionRequest;
import com.bhaumik18.finguard.transaction.dto.TransactionResponse;
import com.bhaumik18.finguard.transaction.entity.Transaction;
import com.bhaumik18.finguard.transaction.mapper.TransactionMapper;
import com.bhaumik18.finguard.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    /**
     * Processes a new financial transaction.
     * * @param request The validated transaction payload
     * @return TransactionResponse containing the generated ID and status
     */
    @Transactional
    public TransactionResponse processTransaction(TransactionRequest request) {
        
        log.info("Initiating transaction from {} to {} for amount {}", 
                 request.sourceAccountId(), request.destinationAccountId(), request.amount());

     // 1. Execute Business Rule Validations
        if (request.sourceAccountId().equalsIgnoreCase(request.destinationAccountId())) {
            log.warn("Transaction failed: Source and destination accounts are identical ({})", request.sourceAccountId());
            throw new IllegalArgumentException("Source and destination accounts cannot be the same.");
        }

        if (request.amount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Transaction failed: Invalid amount ({})", request.amount());
            throw new IllegalArgumentException("Transaction amount must be strictly greater than zero.");
        }
        
        
     // 2. Map DTO to Entity
        Transaction transaction = transactionMapper.toEntity(request);

        // 3. Generate secure Transaction Reference & Set Initial State
        // In an enterprise system, this reference is exposed to the client, while the internal UUID is kept hidden.
        String secureRef = "TXN-" + java.util.UUID.randomUUID().toString().substring(0, 12).toUpperCase();
        transaction.setTransactionReference(secureRef);
        
        // Assuming you have a TransactionStatus enum from yesterday
        transaction.setStatus(com.bhaumik18.finguard.transaction.entity.TransactionStatus.PENDING); 

        // 4. Save to Database
        log.info("Persisting transaction reference: {}", transaction.getTransactionReference());
        Transaction savedTransaction = transactionRepository.save(transaction);

        // 5. Map Entity back to Response DTO
        log.info("Transaction {} successfully created with internal ID: {}", 
                 savedTransaction.getTransactionReference(), savedTransaction.getId());
                 
        return transactionMapper.toResponse(savedTransaction);    }
}