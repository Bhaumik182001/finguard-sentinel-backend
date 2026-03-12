package com.bhaumik18.finguard.transaction.repository;

import org.springframework.data.jpa.domain.Specification;

import com.bhaumik18.finguard.transaction.entity.Transaction;

public class TransactionSpecification {
	
	public static Specification<Transaction> involvesAccount(String accountId){
		return (root, query, cb) -> {
			if(accountId == null || accountId.isBlank()) {
				return null;
			}
			
			return cb.or(
	                cb.equal(root.get("sourceAccountId"), accountId),
	                cb.equal(root.get("destinationAccountId"), accountId)
	            );
		};
	}
	
	public static Specification<Transaction> hasStatus(String status){
		return (root, query, cb) -> {
			if(status == null || status.isBlank()) {
				return null;
			}
			
			return cb.equal(root.get("status"), status);
		};
	}
}
