package com.integration.car.rental.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.integration.car.rental.exception.BusinessServiceException;
import com.integration.car.rental.model.CarBookingStatus;
import com.integration.car.rental.model.CarRentalDetails;
import com.integration.car.rental.util.CarRentalServiceUtility;

/**
 * 
 * @author Sateesh Kota
 * @date 24-Oct-2020
 * This is the implementation of CarRentalService and provides methods for registering 
 * new car, searching for availability of car.
 */

@Component
public class CarRentalService implements RentalService {

	private static final Logger LOGGER = LoggerFactory.getLogger(CarRentalService.class);
	
	
	private static List<String> registeredCarsInventory = new CopyOnWriteArrayList();
	private static Map<String, List<CarRentalDetails>> carAvailabilityInventory = new ConcurrentHashMap<>();
	
	/**
	 * Registers new car
	 * @plateNumber
	 */
	@Override
	public String registerVechile(String plateNumber) throws BusinessServiceException {
		if(plateNumber == null || StringUtils.isEmpty(plateNumber)) {
			throw new BusinessServiceException("Registration plate number should not be empty.");
		}
		if(registeredCarsInventory.contains(plateNumber)) {
			throw new BusinessServiceException("Car is already registered.");
		} else {
			registeredCarsInventory.add(plateNumber);
		}
		LOGGER.info("Car registered successfully with plateNumber = "+plateNumber);
		return "Success";
	}

	/**
	 * add car availability to inventory.
	 * @plateNumber
	 * @from
	 * @to
	 * @pricePerHour
	 */
	@Override
	public String addVehicleAvailabilityToInventory(String plateNumber, String from, String to, double pricePerHour)
			throws BusinessServiceException {
		
		if(CarRentalServiceUtility.isInputValid(from, to) && isValidCar(plateNumber)) {
			LocalDateTime fromDateTime = CarRentalServiceUtility.getFormattedDate(from);
			LocalDateTime toDateTime = CarRentalServiceUtility.getFormattedDate(to);
			CarRentalDetails carRentalDetails = new CarRentalDetails(plateNumber, fromDateTime, toDateTime, pricePerHour, CarBookingStatus.AVAILABLE);;
			if(carAvailabilityInventory.containsKey(plateNumber)) {
				List<CarRentalDetails> listOfAvailableCarTimings = carAvailabilityInventory.get(plateNumber);
				long count = listOfAvailableCarTimings.stream().filter(existingCarRentalDetail -> existingCarRentalDetail.equals(carRentalDetails)).count();
				if(count > 0) {
					throw new BusinessServiceException("Car is alredy added to inventory");
				}
				
				Predicate<CarRentalDetails> isFromDateInRange = car -> (fromDateTime.isAfter(car.getStartTime()) || fromDateTime.isEqual(car.getStartTime())) 
						&& (fromDateTime.isBefore(car.getEndTime()) || fromDateTime.isEqual(car.getStartTime()));
				Predicate<CarRentalDetails> isToDateInRange = car -> (toDateTime.isAfter(car.getStartTime()) || toDateTime.isEqual(car.getStartTime()))
						&& (toDateTime.isBefore(car.getEndTime()) || toDateTime.isEqual(car.getEndTime()));
				Predicate<CarRentalDetails> isOutsideDates = car -> car.getStartTime().isAfter(fromDateTime) && car.getEndTime().isBefore(toDateTime);
				long noOfEntriesWithSameDates = listOfAvailableCarTimings.stream().filter(isFromDateInRange.or(isToDateInRange).or(isOutsideDates)).count();
				if(noOfEntriesWithSameDates == 0) {
					listOfAvailableCarTimings.add(carRentalDetails);
					carAvailabilityInventory.put(plateNumber, listOfAvailableCarTimings);
				} else {
					throw new BusinessServiceException("Car is alredy added to inventory with overlapping dates. Please provide valid dates.");
				}
			} else {
				List<CarRentalDetails> listOfAvailableCars = new ArrayList<>();
				listOfAvailableCars.add(carRentalDetails);
				carAvailabilityInventory.put(plateNumber, listOfAvailableCars);
			}
		}
		LOGGER.info("Car availability successfully added to inventory with plateNumber = "+plateNumber+", from = "+from
				+", to = "+to+ ", price = "+pricePerHour);
		return "Car details added to Inventory";
	}


	/**
	 * search available car details by dates and price.
	 * @from
	 * @to
	 * @pricePerHour
	 */
	@Override
	public List<CarRentalDetails> searchVehicles(String from, String to, double pricePerHour) throws BusinessServiceException {
		List<CarRentalDetails> result = new ArrayList<>();
		if(CarRentalServiceUtility.isInputValid(from, to)) {
			Collection<List<CarRentalDetails>> listOfAvailableCars =  carAvailabilityInventory.values();
			LocalDateTime fromDateTime = CarRentalServiceUtility.getFormattedDate(from);
			LocalDateTime toDateTime = CarRentalServiceUtility.getFormattedDate(to);
			Predicate<CarRentalDetails> filterCars = car -> car.getStartTime().isBefore(fromDateTime) && car.getEndTime().isAfter(toDateTime) && car.getPricePerHour() <= pricePerHour;
			result = listOfAvailableCars.stream().flatMap(Collection::stream).filter(filterCars).collect(Collectors.toList());
		}
		return result;
		
	}

	/**
	 * update rent request to inventory.
	 * @from
	 * @to
	 * @email
	 * @plateNumber
	 */
	@Override
	public String rentVehicle(String from, String to, String email, String plateNumber)
			throws BusinessServiceException {
		
		if(CarRentalServiceUtility.isInputValid(from, to)) {
			if(UserRegistrationService.userInventory.containsKey(email)) {
				boolean result = checkCarAvailabiltyAndBook(plateNumber, from, to);
				if(!result) {
					return "This car is not available at this time.";
				}
			} else {
				throw new BusinessServiceException("User is not registered, Please register user information first.");
			}
		}
		LOGGER.info("Booking request completed successfully with details car = "+plateNumber+", from = "+from
				+", to = "+to+ ", email = "+email);
		return "Success!";
		
	}
	
	/**
	 * 
	 * @param plateNumber
	 * @param from
	 * @param to
	 * @return 	true - if request is completed
	 * 			false - if no cars are avilable.
	 * @throws BusinessServiceException
	 */
	private boolean checkCarAvailabiltyAndBook(String plateNumber, String from, String to) throws BusinessServiceException {
		
		if(isValidCar(plateNumber)) {
			List<CarRentalDetails> listOfAvailableTimings =  carAvailabilityInventory.get(plateNumber);
			
			if(listOfAvailableTimings == null || listOfAvailableTimings.size() == 0) {
				throw new BusinessServiceException("This car is not available for booking yet");
			}
			
			LocalDateTime fromDateTime = CarRentalServiceUtility.getFormattedDate(from);
			LocalDateTime toDateTime = CarRentalServiceUtility.getFormattedDate(to);
			for(CarRentalDetails carDetails : listOfAvailableTimings) {
				if((fromDateTime.isAfter(carDetails.getStartTime()) || fromDateTime.isEqual(carDetails.getStartTime()))
						&& (toDateTime.isEqual(carDetails.getEndTime()) || toDateTime.isBefore(carDetails.getEndTime()))  
						&& carDetails.getCarBookingStatus().equals(CarBookingStatus.AVAILABLE)) {
					carDetails.setCarBookingStatus(CarBookingStatus.BOOKED);
					carAvailabilityInventory.put(plateNumber, listOfAvailableTimings);
					return true;
				}
			}	
		}
		return false;
		
	}	
	
	private boolean isValidCar(String plateNumber) {
		if(registeredCarsInventory.contains(plateNumber)) {
			return true;
		}
		throw new BusinessServiceException("Please register car information first and try.");
	}

}
