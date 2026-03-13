package com.bhaumik18.finguard.config; // Adjust to your actual package

import com.bhaumik18.finguard.account.Account;
import com.bhaumik18.finguard.account.repository.AccountRepository;
import com.bhaumik18.finguard.user.Role;
import com.bhaumik18.finguard.user.User;
import com.bhaumik18.finguard.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component 
@RequiredArgsConstructor
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // Only seed data if the test user doesn't exist yet
        if (userRepository.findByEmail("test@finguard.com").isEmpty()) {
            
            log.info("Seeding database with test user and accounts...");

            // 1. Create the Test User
            User testUser = User.builder()
                    .firstName("Test")
                    .lastName("User")
                    .email("test@finguard.com")
                    .password(passwordEncoder.encode("password"))
                    .role(Role.USER)
                    .build();
            userRepository.save(testUser);

            // 2. Create Two Accounts for this User
            Account accountOne = Account.builder()
                    .accountNumber("ACC-1001")
                    .balance(new BigDecimal("5000.00"))
                    .currency("USD")
                    .user(testUser)
                    .build();

            Account accountTwo = Account.builder()
                    .accountNumber("ACC-9999")
                    .balance(new BigDecimal("1000.00"))
                    .currency("USD")
                    .user(testUser)
                    .build();

            accountRepository.save(accountOne);
            accountRepository.save(accountTwo);

            log.info("Database seeding complete. Test user ready.");
        }
    }
}