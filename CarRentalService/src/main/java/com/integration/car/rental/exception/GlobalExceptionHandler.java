package com.integration.car.rental.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 *
 * @author Sateesh Kota
 * @date 26-Oct-2020
 * Global exception handler.
 *
 */
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(BusinessServiceException.class)
	public ResponseEntity<ExceptionDetails> handleExceptions(BusinessServiceException bse) {
		
		ExceptionDetails exDetails = new ExceptionDetails();
		exDetails.setErrorCode("Application Error");
		exDetails.setErrorMessage(bse.getMessage());
		exDetails.setLocalDateTime(LocalDateTime.now());
		return new ResponseEntity<>(exDetails, HttpStatus.NOT_FOUND);
	}

}
