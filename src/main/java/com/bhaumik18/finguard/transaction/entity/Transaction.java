package com.bhaumik18.finguard.transaction.entity;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import org.hibernate.envers.Audited;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@EntityListeners(AuditingEntityListener.class)
@Audited
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(name = "transaction_reference", unique = true, nullable = false, updatable = false)
	private String transactionReference;
	
	@Column(name = "source_account_id", nullable = false, updatable = false)
	private String sourceAccountId;
	
	@Column(name = "destination_account_id", nullable = false, updatable = false)
	private String destinationAccountId;
	
	@Column(nullable = false, precision = 19, scale = 4, updatable = false)
	private BigDecimal amount;
	
	@Column(nullable = false, length = 3, updatable = false)
	private String currency;
	
	@Enumerated(EnumType.STRING)
	private TransactionStatus status;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "transaction_type", nullable = false, updatable = false)
	private TransactionType type;
	
	@CreatedDate
	@Column(name = "created_at", nullable = false, updatable = false)
	private Instant createdAt;
	
	@LastModifiedDate
	@Column(name = "updated_at", nullable = false)
	private Instant updatedAt;
	
	@Version
	private Integer version;
}
