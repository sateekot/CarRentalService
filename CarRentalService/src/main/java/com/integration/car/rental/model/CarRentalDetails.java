package com.integration.car.rental.model;

import java.time.LocalDateTime;


/**
 * 
 * @author Sateesh Kota
 * @date 24-Oct-2020
 * CarRentalDetails is a model to hold the Car renting information.
 */
public class CarRentalDetails {
	
	private String carId;
	private LocalDateTime startTime;
	private LocalDateTime endTime;
	private double pricePerHour;
	private CarBookingStatus carBookingStatus;
	
	public CarRentalDetails() {}
	
	public CarRentalDetails(String carId, LocalDateTime startTime, LocalDateTime endTime, double pricePerHour,
			CarBookingStatus carBookingStatus) {
		this.carId = carId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.pricePerHour = pricePerHour;
		this.carBookingStatus = carBookingStatus;
	}
	public String getCarId() {
		return carId;
	}
	public void setCarId(String carId) {
		this.carId = carId;
	}
	public LocalDateTime getStartTime() {
		return startTime;
	}
	public void setStartTime(LocalDateTime startTime) {
		this.startTime = startTime;
	}
	public LocalDateTime getEndTime() {
		return endTime;
	}
	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}
	public double getPricePerHour() {
		return pricePerHour;
	}
	public void setPricePerHour(double pricePerHour) {
		this.pricePerHour = pricePerHour;
	}
	public CarBookingStatus getCarBookingStatus() {
		return carBookingStatus;
	}
	public void setCarBookingStatus(CarBookingStatus carBookingStatus) {
		this.carBookingStatus = carBookingStatus;
	}
	
	

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((carId == null) ? 0 : carId.hashCode());
		result = prime * result + ((endTime == null) ? 0 : endTime.hashCode());
		result = prime * result + ((startTime == null) ? 0 : startTime.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CarRentalDetails other = (CarRentalDetails) obj;
		if (carId == null) {
			if (other.carId != null)
				return false;
		} else if (!carId.equals(other.carId))
			return false;
		if (endTime == null) {
			if (other.endTime != null)
				return false;
		} else if (!endTime.equals(other.endTime))
			return false;
		if (startTime == null) {
			if (other.startTime != null)
				return false;
		} else if (!startTime.equals(other.startTime))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CarRentalDetails [carId=" + carId + ", startTime=" + startTime + ", endTime=" + endTime
				+ ", pricePerHour=" + pricePerHour + ", carBookingStatus=" + carBookingStatus + "]";
	}
	
	
	
}
