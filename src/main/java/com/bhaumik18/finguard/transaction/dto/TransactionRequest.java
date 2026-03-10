package com.bhaumik18.finguard.transaction.dto;

import java.math.BigDecimal;

import com.bhaumik18.finguard.transaction.entity.TransactionType;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record TransactionRequest(
		
		@NotBlank(message = "Source account ID is mandatory")
		String sourceAccountId,
		
		@NotBlank(message = "Destination account ID is mandatory")
		String destinationAccountId,
		
		@NotNull(message = "Amount is mandatory")
		@DecimalMin(value = "0.01", message = "Transaction amount must be greater than zero")
		BigDecimal amount,
		
		@NotBlank(message = "Currency code is mandatory")
		@Pattern(regexp = "^[A-Z]{3}$", message = "Currency must be valid 3-letter ISO 4217 code")
		String currency,
		
		@NotNull(message = "Transaction type is mandatory")
		TransactionType type
) {}
