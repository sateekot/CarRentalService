package com.integration.car.rental.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.integration.car.rental.exception.BusinessServiceException;
import com.integration.car.rental.model.User;
import com.integration.car.rental.service.UserRegistrationService;

/**
 * 
 * @author Sateesh Kota
 * @date 26-Oct-2020
 * UserRegisterControler provides REST API to register new users to the system.
 */

@RestController
public class UserRegisterControler {

	private static final Logger LOGGER = LoggerFactory.getLogger(UserRegisterControler.class);
	
	@Autowired
	private UserRegistrationService userRegistrationService;
	
	@RequestMapping(path = "/registerUser", method = RequestMethod.POST)
	public String registerUser(@RequestBody User user) throws BusinessServiceException {
		LOGGER.debug("Received request for registering a new user with user information ="+user);
		return userRegistrationService.registerUser(user);
	}
	
}
