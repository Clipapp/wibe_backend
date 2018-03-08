package com.wibe.backend.config;

import java.util.ArrayList;
import java.util.List;

public class UserEndpoints {
	
	public List<String> userEndpoints;
	
	public UserEndpoints(String uid){
		userEndpoints = new ArrayList<String>();
		userEndpoints.add("GET:/wibers" + uid);
		userEndpoints.add("POST:/otp/verify");
		userEndpoints.add("POST:/login/facebook");
		userEndpoints.add("POST:/login/google");
		userEndpoints.add("POST:/error");
		userEndpoints.add("GET:/error");
		userEndpoints.add("DELETE:/error");
		userEndpoints.add("PUT:/error");
		userEndpoints.add("/");
	}

}
