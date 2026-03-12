package com.bhaumik18.finguard.security.dto;

public record RegisterRequest(
		String firstName,
	    String lastName,
	    String email,
	    String password
) {}
