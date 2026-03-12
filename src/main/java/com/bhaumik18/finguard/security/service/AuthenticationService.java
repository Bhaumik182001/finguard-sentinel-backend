package com.bhaumik18.finguard.security.service;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bhaumik18.finguard.security.dto.AuthenticationResponse;
import com.bhaumik18.finguard.security.dto.RegisterRequest;
import com.bhaumik18.finguard.user.Role;
import com.bhaumik18.finguard.user.User;
import com.bhaumik18.finguard.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
	private final UserRepository repository;
	private final PasswordEncoder passwordEncoder;
	private final JwtService jwtService;
	
	public AuthenticationResponse register(RegisterRequest request) {
		var user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(Role.USER)
                .build();
		
		repository.save(user);
		var jwtToken = jwtService.generateToken(user);
		return new AuthenticationResponse(jwtToken);
	}
}
