package com.bhaumik18.finguard.config;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.bhaumik18.finguard.transaction.dto.TransactionRequest;
import com.bhaumik18.finguard.transaction.entity.Transaction;
import com.bhaumik18.finguard.transaction.entity.TransactionStatus;
import com.bhaumik18.finguard.transaction.entity.TransactionType;
import com.bhaumik18.finguard.transaction.mapper.TransactionMapper;
import com.bhaumik18.finguard.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;


//@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder {
	
	private final TransactionRepository repository;
	private final TransactionMapper mapper;
	
	@Bean
	CommandLineRunner initDatabase() {
		return args -> {
			
			
			TransactionRequest request = new TransactionRequest(
                    "ACC-1001",
                    "ACC-9999",
                    new BigDecimal("2500.00"),
                    "USD",
                    TransactionType.TRANSFER
            );
			
			Transaction txn = mapper.toEntity(request);
			
			txn.setTransactionReference("TXN-" + UUID.randomUUID().toString().substring(0, 8));
			txn.setStatus(TransactionStatus.PENDING);
			
			repository.save(txn);
			System.out.println("--- Milestone 2 Smoke Test Successful ---");
		};
	}
}
