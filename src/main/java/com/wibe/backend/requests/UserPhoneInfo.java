package com.wibe.backend.requests;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="userPhoneInfo")
public class UserPhoneInfo {
	
	@Id
	private String _id;
	
	private long uid;
	
	private List<String> installedApps;
	
	private String networkType;
	
	private String osLanguage;
	
	private String keyboardLanguage;

	public long getUid() {
		return uid;
	}

	public void setUid(long uid) {
		this.uid = uid;
	}

	public List<String> getInstalledApps() {
		return installedApps;
	}

	public void setInstalledApps(List<String> installedApps) {
		this.installedApps = installedApps;
	}

	public String getNetworkType() {
		return networkType;
	}

	public void setNetworkType(String networkType) {
		this.networkType = networkType;
	}

	public String getOsLanguage() {
		return osLanguage;
	}

	public void setOsLanguage(String osLanguage) {
		this.osLanguage = osLanguage;
	}

	public String getKeyboardLanguage() {
		return keyboardLanguage;
	}

	public void setKeyboardLanguage(String keyboardLanguage) {
		this.keyboardLanguage = keyboardLanguage;
	}
	
	

}
