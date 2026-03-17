package com.bhaumik18.finguard.transaction.repository;

import com.bhaumik18.finguard.transaction.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction>{

	Optional<Transaction> findByTransactionReference(String transactionReference);
	
}
