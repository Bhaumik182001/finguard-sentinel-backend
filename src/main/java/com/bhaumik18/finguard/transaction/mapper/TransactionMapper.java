package com.bhaumik18.finguard.transaction.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import com.bhaumik18.finguard.transaction.dto.TransactionRequest;
import com.bhaumik18.finguard.transaction.dto.TransactionResponse;
import com.bhaumik18.finguard.transaction.entity.Transaction;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface TransactionMapper {
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "status", ignore = true)
	@Mapping(target = "transactionReference", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "updatedAt", ignore = true)
	@Mapping(target = "version", ignore = true)
	Transaction toEntity(TransactionRequest request);
	
	TransactionResponse toResponse(Transaction transaction);
}
