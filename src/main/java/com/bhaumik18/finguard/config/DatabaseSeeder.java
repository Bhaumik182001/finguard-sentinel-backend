package com.bhaumik18.finguard.config;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bhaumik18.finguard.transaction.entity.Transaction;
import com.bhaumik18.finguard.transaction.entity.TransactionStatus;
import com.bhaumik18.finguard.transaction.entity.TransactionType;
import com.bhaumik18.finguard.transaction.repository.TransactionRepository;

@Configuration
public class DatabaseSeeder {
	
	@Bean
	CommandLineRunner initDatabase(TransactionRepository repository) {
		return args -> {
			System.out.println("--- Bootstrapping FinGuard Sentinel ---");
			
			Transaction txn = Transaction.builder()
                    .transactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8))
                    .sourceAccountId("ACC-1001")
                    .destinationAccountId("ACC-9999")
                    .amount(new BigDecimal("1500.0000"))
                    .currency("USD")
                    .status(TransactionStatus.PENDING)
                    .type(TransactionType.TRANSFER)
                    .build();
			
			Transaction savedTxn = repository.save(txn);
			System.out.println("Created: " + savedTxn.getTransactionReference() + " | Status: " + savedTxn.getStatus());
			
			Thread.sleep(2000);
			
			savedTxn.setStatus(TransactionStatus.COMPLETED);
			repository.save(savedTxn);
			System.out.println("Updated: " + savedTxn.getTransactionReference() + " | Status: " + savedTxn.getStatus());
		
			System.out.println("--- Bootstrapping Complete. Check H2 Console! ---");
		};
	}
}
