package com.integration.car.rental.service;

import java.util.List;

import com.integration.car.rental.exception.BusinessServiceException;
import com.integration.car.rental.model.CarRentalDetails;

/**
 * 
 * @author Sateesh Kota
 * @date 24-Oct-2020.
 */
public interface RentalService {

	public String registerVechile(String plateNumber) throws BusinessServiceException;
	public String addVehicleAvailabilityToInventory(String plateNumber, String from, String to, double pricePerHour) throws BusinessServiceException;
	public List<CarRentalDetails> searchVehicles(String from, String to, double pricePerHour) throws BusinessServiceException;
	public String rentVehicle(String from, String to, String email, String plateNumber) throws BusinessServiceException;
}
