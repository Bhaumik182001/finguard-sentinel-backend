package com.bhaumik18.finguard.transaction.listener;

import com.bhaumik18.finguard.transaction.entity.Transaction;
import com.bhaumik18.finguard.transaction.entity.TransactionStatus;
import com.bhaumik18.finguard.transaction.event.TransactionCompletedEvent;
import com.bhaumik18.finguard.transaction.repository.TransactionRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener; // NEW

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionNotificationListener {
	
	private final TransactionRepository transactionRepository;

    @Async("notificationExecutor")
    // This tells Spring: "Do NOT fire this event until the database commit is 100% successful!"
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) 
    public void handleTransactionCompleted(TransactionCompletedEvent event) {
        
        log.info("--- ASYNC WORKER STARTED ---");
        log.info("Simulating connection to 3rd Party Bank Processor...");
        
        try {
            Thread.sleep(3000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Transaction transaction = transactionRepository.findByTransactionReference(event.transactionId())
                .orElseThrow(() -> new RuntimeException("Transaction not found for Async Update"));

        transaction.setStatus(TransactionStatus.COMPLETED);
        transactionRepository.save(transaction);
        
        log.info("EMAIL SENT: Successfully notified {} about transfer of ${} (Ref: {})", 
                 event.userEmail(), event.amount(), event.transactionId());
        log.info("--- ASYNC WORKER FINISHED ---");
    }
}
