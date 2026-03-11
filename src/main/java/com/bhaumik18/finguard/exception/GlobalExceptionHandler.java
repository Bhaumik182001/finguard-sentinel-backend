package com.bhaumik18.finguard.exception;

import java.net.URI;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import io.swagger.v3.oas.annotations.Hidden;

@Hidden
@RestControllerAdvice
public class GlobalExceptionHandler {
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ProblemDetail handleValidationErrors(MethodArgumentNotValidException ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, "Invalid request content");
		problemDetail.setTitle("Validation Failed");
		problemDetail.setType(URI.create("https://finguard.com/errors/validation-failed"));
		problemDetail.setProperty("Timestamp", Instant.now());
		
		var errors = ex.getBindingResult().getFieldErrors()
				.stream().map(error -> error.getField() + ": " + error.getDefaultMessage());
		
		problemDetail.setProperty("errors", errors);
		return problemDetail;
	}
	
	@ExceptionHandler(Exception.class)
	public ProblemDetail handleGeneralException(Exception ex) {
		ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
		problemDetail.setTitle("Server Error");
		problemDetail.setProperty("timestamp", Instant.now());
		return problemDetail;
	}
}
