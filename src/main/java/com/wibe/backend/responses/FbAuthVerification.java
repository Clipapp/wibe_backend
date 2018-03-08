package com.wibe.backend.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FbAuthVerification {
	
	private String id;
	
	private Phone phone;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Phone getPhone() {
		return phone;
	}

	public void setPhone(Phone phone) {
		this.phone = phone;
	}

}
