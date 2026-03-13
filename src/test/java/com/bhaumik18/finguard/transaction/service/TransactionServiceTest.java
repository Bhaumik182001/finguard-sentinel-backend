package com.bhaumik18.finguard.transaction.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.access.AccessDeniedException;

import com.bhaumik18.finguard.account.Account;
import com.bhaumik18.finguard.account.repository.AccountRepository;
import com.bhaumik18.finguard.exception.BusinessRuleException;
import com.bhaumik18.finguard.transaction.dto.TransactionRequest;
import com.bhaumik18.finguard.transaction.dto.TransactionResponse;
import com.bhaumik18.finguard.transaction.entity.Transaction;
import com.bhaumik18.finguard.transaction.entity.TransactionType;
import com.bhaumik18.finguard.transaction.mapper.TransactionMapper;
import com.bhaumik18.finguard.transaction.repository.TransactionRepository;
import com.bhaumik18.finguard.user.User;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
	
	@Mock private AccountRepository accountRepository;
	@Mock private TransactionRepository transactionRepository;
	@Mock private TransactionMapper transactionMapper;
	@Mock private ApplicationEventPublisher eventPublisher;
	
	@InjectMocks
	private TransactionService transactionService;
	
	private User testUser;
	private Account sourceAccount;
	private Account destAccount;
	private TransactionRequest request;
	
	@BeforeEach
	void setUp() {
		testUser = User.builder().email("test@finguard.com").build();

        sourceAccount = Account.builder()
                .accountNumber("ACC-1001")
                .balance(new BigDecimal("5000.00"))
                .user(testUser)
                .build();

        destAccount = Account.builder()
                .accountNumber("ACC-9999")
                .balance(new BigDecimal("1000.00"))
                .user(testUser)
                .build();

        request = new TransactionRequest("ACC-1001", "ACC-9999", new BigDecimal("1000.00"), "USD", TransactionType.TRANSFER);
	}
	
	@Test
    void shouldProcessTransactionSuccessfully() {
        // Arrange: Tell the "fake" database what to return when asked
        when(accountRepository.findByAccountNumber("ACC-1001")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("ACC-9999")).thenReturn(Optional.of(destAccount));
        
        Transaction mockTransaction = new Transaction();
        mockTransaction.setAmount(new BigDecimal("1000.00"));
        when(transactionMapper.toEntity(request)).thenReturn(mockTransaction);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(mockTransaction);
        
        // Act: Run the real business logic
        transactionService.processTransaction(request, "test@finguard.com");

        // Assert: Verify the math was correct
        assertEquals(new BigDecimal("4000.00"), sourceAccount.getBalance()); 
        assertEquals(new BigDecimal("2000.00"), destAccount.getBalance()); 
        
        // Verify the async event was shouted out exactly once
        verify(eventPublisher, times(1)).publishEvent(any(Object.class));
    }

    @Test
    void shouldThrowExceptionWhenInsufficientFunds() {
        // Arrange: Rig the source account to only have $500
        sourceAccount.setBalance(new BigDecimal("500.00")); 
        when(accountRepository.findByAccountNumber("ACC-1001")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("ACC-9999")).thenReturn(Optional.of(destAccount));

        // Act & Assert: Verify that attempting a $1000 transfer throws our specific error
        BusinessRuleException exception = assertThrows(BusinessRuleException.class, () -> {
            transactionService.processTransaction(request, "test@finguard.com");
        });

        assertEquals("Insufficient funds in account: ACC-1001", exception.getMessage());
        
        // Ensure the system STOPPED and never tried to save a bad transaction
        verify(transactionRepository, never()).save(any()); 
    }
    
    @Test
    void shouldThrowExceptionWhenUserDoesNotOwnSourceAccount() {
        // Arrange: The accounts exist and belong to "test@finguard.com"
        when(accountRepository.findByAccountNumber("ACC-1001")).thenReturn(Optional.of(sourceAccount));
        when(accountRepository.findByAccountNumber("ACC-9999")).thenReturn(Optional.of(destAccount));

        // Act & Assert: A different user ("hacker@finguard.com") tries to initiate the transfer
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            transactionService.processTransaction(request, "hacker@finguard.com"); 
        });

        // Verify the exact security message was thrown
        assertEquals("You are not authorized to transfer funds from this account.", exception.getMessage());
        
        // Ensure the system immediately blocked the transaction from saving or publishing
        verify(transactionRepository, never()).save(any()); 
        verify(eventPublisher, never()).publishEvent(any(Object.class));
    }
}
