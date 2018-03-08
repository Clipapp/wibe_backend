package com.wibe.backend.responses;

public class Phone {
	
	private String number;
	
	private String country_prefix;
	
	private String national_number;

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCountry_prefix() {
		return country_prefix;
	}

	public void setCountry_prefix(String country_prefix) {
		this.country_prefix = country_prefix;
	}

	public String getNational_number() {
		return national_number;
	}

	public void setNational_number(String national_number) {
		this.national_number = national_number;
	}

}
