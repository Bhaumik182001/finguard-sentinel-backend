package com.bhaumik18.finguard.transaction.listener;

import com.bhaumik18.finguard.transaction.event.TransactionCompletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener; // NEW

@Slf4j
@Component
public class TransactionNotificationListener {

    @Async("notificationExecutor")
    // This tells Spring: "Do NOT fire this event until the database commit is 100% successful!"
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT) 
    public void handleTransactionCompleted(TransactionCompletedEvent event) {
        
        log.info("--- ASYNC WORKER STARTED ---");
        log.info("Simulating connection to Email Server (AWS SES / SendGrid)...");
        
        try {
            Thread.sleep(3000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        log.info("EMAIL SENT: Successfully notified {} about transfer of ${} (Ref: {})", 
                 event.userEmail(), event.amount(), event.transactionId());
        log.info("--- ASYNC WORKER FINISHED ---");
    }
}
