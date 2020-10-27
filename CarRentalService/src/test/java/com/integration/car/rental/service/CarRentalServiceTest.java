package com.integration.car.rental.service;


import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.integration.car.rental.exception.BusinessServiceException;
import com.integration.car.rental.model.CarRentalDetails;
import com.integration.car.rental.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

@SpringBootTest
public class CarRentalServiceTest {

	@Autowired
	private CarRentalService carRentalService;
	
	@Autowired
	private UserRegistrationService userRegistrationService;
	
	public static String fromDate = "2020-10-26 10:00";
	public static String toDate = "2020-10-30 10:00";
	
		

	@Test
	public void testRegisterCar() throws BusinessServiceException {
		
			String plateNumber = "INF001";
			String result = registerCar(plateNumber);
			assertEquals("Success", result);

	}
	
	@Test
	public void testRegisterCarWithNullAndSamePlate() throws BusinessServiceException {
		String emptyPlateNumber = "";
		Exception exception = assertThrows(BusinessServiceException.class, () -> carRentalService.registerVechile(emptyPlateNumber));
		assertTrue(exception.getMessage().contains("Registration plate number should not be empty."));
		String plateNumber = "INF002";
		carRentalService.registerVechile(plateNumber);
		exception = assertThrows(BusinessServiceException.class, () -> carRentalService.registerVechile(plateNumber));
		assertTrue(exception.getMessage().contains("Car is already registered."));

	}
	
	@Test
	public void testAddVehicleAvailabilityToInventory() throws BusinessServiceException {
		String plateNumber = "INF003";
		String result = registerAndAddAvailabilityToInventory(plateNumber, fromDate, toDate, 30);
		String expectedMessage = "Car details added to Inventory";
		assertEquals(expectedMessage, result);
	}
	
	@Test
	public void testAddVehicleAvailabilityToInventoryWithInvalidDates() throws BusinessServiceException {
		String plateNumber = "INF004";
		
		Exception exception = assertThrows(BusinessServiceException.class, () -> registerAndAddAvailabilityToInventory(plateNumber, "", "", 30));
		assertTrue(exception.getMessage().contains("Input dates should not be empty."));
		
		String invalidFrom = "2020-10-26 10:00";
		String invalidTo = "2020-10-22 10:00";
		exception = assertThrows(BusinessServiceException.class, () -> carRentalService.addVehicleAvailabilityToInventory(plateNumber, invalidFrom, invalidTo, 30));
		assertTrue(exception.getMessage().contains("From date should be previous to To date."));
	}
	
	@Test
	public void testAddVehicleAvailabilityToInventoryWithAlreadyPresent() throws BusinessServiceException {
		String plateNumber = "INF005";		
		String result = registerAndAddAvailabilityToInventory(plateNumber, fromDate, toDate, 30);
		String expectedMessage = "Car details added to Inventory";
		assertEquals(expectedMessage, result);
		
		fromDate = "2020-10-26 10:00";
		toDate = "2020-10-30 10:00";
		Exception exception = assertThrows(BusinessServiceException.class, () -> carRentalService.addVehicleAvailabilityToInventory(plateNumber, fromDate, toDate, 30));
		assertTrue(exception.getMessage().contains("Car is alredy added to inventory"));
	}
	
	@Test
	
	public void testSearchInventory() throws BusinessServiceException {
		addSampleData();
		List<CarRentalDetails> searchResult = carRentalService.searchVehicles("2020-10-29 09:00", "2020-10-30 09:00", 10);
		assertEquals(3, searchResult.size());
	}
	
	@Test
	@Order(2)
	public void testRentACar() throws BusinessServiceException {
		String userRegistrationStatus = registerUser();
		assertEquals("User information saved successfully.", userRegistrationStatus);
		String result = rentACar();
		assertEquals("Success!", result);
		
	}
	
	private String registerCar(String plateNumber) throws BusinessServiceException {
		return carRentalService.registerVechile(plateNumber);
	}
	
	
	private String registerAndAddAvailabilityToInventory(String plateNumber, String fromDate, String toDate, double pricePerHour) throws BusinessServiceException {
		carRentalService.registerVechile(plateNumber);
		return carRentalService.addVehicleAvailabilityToInventory(plateNumber, fromDate, toDate, pricePerHour);
	}
	
	private void addSampleData() throws BusinessServiceException {
		registerAndAddAvailabilityToInventory("INF101", "2020-10-26 10:00", "2020-10-30 10:00", 4);
		registerAndAddAvailabilityToInventory("INF102", "2020-10-27 10:00", "2020-11-01 10:00", 5);
		registerAndAddAvailabilityToInventory("INF103", "2020-10-28 10:00", "2020-11-02 10:00", 6);
		registerAndAddAvailabilityToInventory("INF104", "2020-10-26 10:00", "2020-11-01 10:00", 12);
		registerAndAddAvailabilityToInventory("INF105", "2020-10-26 10:00", "2020-11-05 10:00", 12);
	}
	
	private String registerUser() throws BusinessServiceException {
		User user = new User();
		user.setEmail("user@email.com");
		user.setFirstName("User");
		user.setLastName("Junit");
		return userRegistrationService.registerUser(user);
	}
	
	private String rentACar() throws BusinessServiceException {
		registerAndAddAvailabilityToInventory("INF107", "2020-10-26 10:00", "2020-10-30 10:00", 4);
		return carRentalService.rentVehicle("2020-10-29 09:00", "2020-10-30 09:00", "user@email.com", "INF107");
	}
}
