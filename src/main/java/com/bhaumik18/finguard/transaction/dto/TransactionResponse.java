package com.bhaumik18.finguard.transaction.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.bhaumik18.finguard.transaction.entity.TransactionStatus;
import com.bhaumik18.finguard.transaction.entity.TransactionType;

public record TransactionResponse(
		UUID id,
		String transactionReference,
		String sourceAccountId,
		String destinationAccountId,
		BigDecimal amount,
		String currency,
		TransactionStatus status,
		TransactionType type,
		Instant createdAt,
		Instant updatedAt,
		Integer version
) {}
