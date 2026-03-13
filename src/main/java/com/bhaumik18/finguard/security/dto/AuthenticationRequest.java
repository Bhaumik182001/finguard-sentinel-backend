package com.bhaumik18.finguard.security.dto;

public record AuthenticationRequest(
    String email,
    String password
) {}