package com.bhaumik18.finguard.transaction.event;

import java.math.BigDecimal;

public record TransactionCompletedEvent(
    String transactionId,
    String userEmail,
    BigDecimal amount
) {}