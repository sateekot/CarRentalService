package com.integration.car.rental.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * 
 * @author Sateesh Kota
 * @date 24-Oct-2020
 * BusinessServiceException will handle all business related exceptions.
 */

public class BusinessServiceException extends RuntimeException {

	private String message;
	
	public BusinessServiceException(String message) {
		super(message);
	}
	
	public BusinessServiceException(String message, Throwable cause) {
		super(message, cause);
	}
}
