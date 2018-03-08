package com.wibe.backend.config;

public final class AppConfig {
	
	private static AppConfig config;
	
	public static final String fbAppId = "239332266472176";
	
	public static final String fbAppSecret = "9ccb3660e057e1c0ef83ba2bcdeebb85";
	
	public static final String thumbnailDomain = "https://d1iozq4k4quu4.cloudfront.net/";
	
	public static final String wibeDomain ="https://d1oeqqihcawq66.cloudfront.net/";
	
	public static final String imageDomain = "https://d1hfq26c03hnox.cloudfront.net/";
	
	public static final String imageThumbDomain = "https://d2df764efu4nbt.cloudfront.net/";
	
	public static final String fbAuthTokenUrl = "https://graph.accountkit.com/v1.1/"
			+ "access_token";
	
	public static final String fbOTPVerificationUrl = "https://graph.accountkit.com/v1.1/me/";
	
	public static final String FcmKey = "AAAAIl4K-m4:APA91bHg7leSu4RAQtOaAtzggG6RyRdJOozzkhtcfWp0Fwa_7ufsiwl6FLyHZrlYjnl0UqgW_RW3bwadYc47ATVCP6gGKxmmdRxB0C7HyVOVt7Jny7q-691VaFj58jDUQHwy4tH7WBxQ";

	public static final String firebaseDb = "https://wibeapp-3a9b5.firebaseio.com/";
	
	public static final String firebaseSecret = "tsm17jp0K5fW8v7sggKkhKVTkHvT0Gbzz7vd8tmg";
	
	public static final String messageKeySecret = "Encrypt Messages"; //16bytes
	
	public static final String messageSecret = "bhi daal do yaar"; //16bytes
	
	public static final String appSecret = "Random Shit";
	
	public static final String messageIVector = "Random IV Vector"; //16bytes
	
	private AppConfig(){
		
	}
	
	public static final AppConfig getInstance (){
		if (config == null){
			config = new AppConfig();
		}
		return config;
	}
}
