package com.integration.car.rental.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.integration.car.rental.exception.BusinessServiceException;
import com.integration.car.rental.model.CarRentalDetails;
import com.integration.car.rental.service.RentalService;

/**
 * 
 * @author Sateesh Kota
 * @date 24-Oct-2020
 * CarManagementController provides all the REST APIs over all the services like
 * adding new car to inventory, checking availability of car for rental.
 */

@RestController
public class CarRentalController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CarRentalController.class);
	
	@Autowired
	private RentalService rentalService;
	
	@RequestMapping(path = "/registerCar", method = RequestMethod.POST)
	public String registerCar(@RequestParam("plateNumber") String plateNumber) throws BusinessServiceException {
		LOGGER.debug("Received request for registering the new car with plate number ="+plateNumber);
		return rentalService.registerVechile(plateNumber);
	}
	
	
	@RequestMapping(path = "/registerCarAvailability", method = RequestMethod.POST)
	public String registerAvailability(@RequestParam("plateNumber") String plateNumber, @RequestParam("from") String from, 
			@RequestParam("to") String to, @RequestParam("price") double pricePerHour) throws BusinessServiceException {
		LOGGER.debug("Received request for registering car availability with plateNumber = "+plateNumber+", from = "+from
				+", to = "+to+ ", price = "+pricePerHour);
		return rentalService.addVehicleAvailabilityToInventory(plateNumber, from, to, pricePerHour);
	}
	
	@RequestMapping(path = "/search", method = RequestMethod.GET)
	public List<CarRentalDetails> searchForCars(@RequestParam("from") String from, 
			@RequestParam("to") String to, @RequestParam("price") double pricePerHour) throws BusinessServiceException {
		LOGGER.debug("Received request to get available cars information with from = "+from
				+", to = "+to+ ", price = "+pricePerHour);
		return rentalService.searchVehicles(from, to, pricePerHour);
	}
	
	@RequestMapping(path = "/bookCar", method = RequestMethod.POST)
	public String rentACar(@RequestParam("plateNumber") String plateNumber, @RequestParam("from") String from, 
			@RequestParam("to") String to, @RequestParam("email") String email) throws BusinessServiceException {
		LOGGER.debug("Received request to book a car with details car = "+plateNumber+", from = "+from
				+", to = "+to+ ", email = "+email);
		return rentalService.rentVehicle(from, to, email, plateNumber);
	}
	
	
}
