package com.bhaumik18.finguard.security.service;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bhaumik18.finguard.security.dto.AuthenticationRequest;
import com.bhaumik18.finguard.security.dto.AuthenticationResponse;
import com.bhaumik18.finguard.security.dto.RegisterRequest;
import com.bhaumik18.finguard.user.Role;
import com.bhaumik18.finguard.user.User;
import com.bhaumik18.finguard.user.repository.UserRepository;
// Make sure to import your Account and AccountRepository here!
import com.bhaumik18.finguard.account.entity.Account; 
import com.bhaumik18.finguard.account.repository.AccountRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;
	
	// ADDED: Inject the AccountRepository
	private final AccountRepository accountRepository; 
	
	public AuthenticationResponse register(RegisterRequest request) {
		var user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
		
		// 1. Save the new user and capture the saved instance
		var savedUser = repository.save(user);
		
		// 2. ADDED: Auto-provision Checking Account
		Account checking = new Account();
		checking.setUser(savedUser);
		checking.setAccountNumber("CHK-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		checking.setBalance(BigDecimal.valueOf(5000.00));
		checking.setCurrency("USD");
		accountRepository.save(checking);

		// 3. ADDED: Auto-provision Savings Account
		Account savings = new Account();
		savings.setUser(savedUser);
		savings.setAccountNumber("SAV-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
		savings.setBalance(BigDecimal.valueOf(1500.00));
		savings.setCurrency("USD");
		accountRepository.save(savings);

		// 4. Generate the JWT using the saved user
		var jwtToken = jwtService.generateToken(savedUser);
		return new AuthenticationResponse(jwtToken);
	}
	
	public AuthenticationResponse authenticate(AuthenticationRequest request) {
		authenticationManager.authenticate(
			new UsernamePasswordAuthenticationToken(
					request.email(),
					request.password()
			)
		);
		
		var user = repository.findByEmail(request.email()).orElseThrow();
		var jwtToken = jwtService.generateToken(user);
		return new AuthenticationResponse(jwtToken);
	}
}