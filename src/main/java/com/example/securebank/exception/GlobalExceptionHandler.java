package com.example.securebank.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiErrorResponse> handleValidationException(
	        MethodArgumentNotValidException ex
	) {
	    String message = ex.getBindingResult()
	            .getFieldErrors()
	            .get(0)
	            .getDefaultMessage();

	    ApiErrorResponse error = new ApiErrorResponse(
	            message,
	            HttpStatus.BAD_REQUEST.value(),
	            LocalDateTime.now()
	    );

	    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}
}