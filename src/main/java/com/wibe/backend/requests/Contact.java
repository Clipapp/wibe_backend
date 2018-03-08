package com.wibe.backend.requests;

public class Contact{
	
	private String name;
	
	private String number;
	
	private boolean isSMSContact;
	
	private String lookupKey;
	
	private boolean registered;
	
	public Contact(){
		
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public boolean isRegistered() {
		return registered;
	}

	public void setRegistered(boolean registered) {
		this.registered = registered;
	}

	public boolean isSMSContact() {
		return isSMSContact;
	}

	public void setSMSContact(boolean isSMSContact) {
		this.isSMSContact = isSMSContact;
	}

	public String getLookupKey() {
		return lookupKey;
	}

	public void setLookupKey(String lookupKey) {
		this.lookupKey = lookupKey;
	}
}
