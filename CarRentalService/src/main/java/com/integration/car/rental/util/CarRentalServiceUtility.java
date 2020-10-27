package com.integration.car.rental.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.util.StringUtils;

import com.integration.car.rental.exception.BusinessServiceException;
import com.integration.car.rental.model.User;

/**
 * 
 * @author Sateesh Kota
 * @date 24-Oct-2020.
 * CarRentalServiceUtility provides utility methods like validation, conversions to the all the services.
 */
public class CarRentalServiceUtility {

	
	public static LocalDateTime getFormattedDate(String date) throws BusinessServiceException {
		if(date == null || StringUtils.isEmpty(date)) {
			throw new BusinessServiceException("Date should not be null or empty.");
		}
		try {
			DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
			return LocalDateTime.parse(date, dateTimeFormatter);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BusinessServiceException("Please provide date in yyyy-mm-dd HH:mm format.");
		}
		
	}
	
	public static boolean isInputValid(String fromDate, String toDate) throws BusinessServiceException {
		if(StringUtils.isEmpty(fromDate) || StringUtils.isEmpty(toDate)) {
			throw new BusinessServiceException("Input dates should not be empty.");
		}
		if(getFormattedDate(fromDate).isAfter(getFormattedDate(toDate))) {
			throw new BusinessServiceException("From date should be previous to To date.");
		}
		return true;
	}
	
	public static boolean validateUser(User user) throws BusinessServiceException {
		
		if(StringUtils.isEmpty(user.getEmail())) {
			throw new BusinessServiceException("User email should not be empty.");
		}
		return true;
	}
}
