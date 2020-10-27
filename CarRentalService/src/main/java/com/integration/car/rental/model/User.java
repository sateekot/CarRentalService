package com.integration.car.rental.model;

/**
 * 
 * @author Sateesh Kota
 * @date 24-Oct-2020
 * User is a model to hold the user information.
 */
public class User {

	private String email;
	private String firstName;
	private String lastName;
	
	
	public User() {}

	public User(Integer userId, String firstName, String lastName, String email) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [email=" + email + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
	
	
	
}
