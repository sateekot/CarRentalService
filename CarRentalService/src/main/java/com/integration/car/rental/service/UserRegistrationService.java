package com.integration.car.rental.service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.integration.car.rental.exception.BusinessServiceException;
import com.integration.car.rental.model.User;
import com.integration.car.rental.util.CarRentalServiceUtility;

/**
 *
 * @author Sateesh Kota
 * @date 25-Oct-2020
 * UserRegistrationService is provides API to register new user.
 */
@Component
public class UserRegistrationService {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRegistrationService.class);
	
	public static Map<String, User> userInventory = new ConcurrentHashMap<>();
	
	public String registerUser(User user) throws BusinessServiceException {
		
		if(CarRentalServiceUtility.validateUser(user)) {
			userInventory.put(user.getEmail(), user);
		}
		String successMessage = "User information saved successfully.";
		LOGGER.info(successMessage+" User details :: "+user);
		return successMessage;
	}
}
