package com.bhaumik18.finguard.transaction.repository;

import com.bhaumik18.finguard.transaction.entity.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction>{

	Optional<Transaction> findByTransactionReference(String transactionReference);
	Page<Transaction> findBySourceAccountIdInOrDestinationAccountIdIn(List<String> sourceIds, List<String> destIds, Pageable pageable);
}
