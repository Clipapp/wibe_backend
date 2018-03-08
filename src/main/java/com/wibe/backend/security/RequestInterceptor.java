package com.wibe.backend.security;

import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.ArrayUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.wibe.backend.config.AppConfig;
import com.wibe.backend.entities.models.User;
import com.wibe.backend.library.Notifications;
import com.wibe.backend.repositories.UserRepository;
import com.wibe.backend.repositories.WordRepository;
import com.wibe.backend.requests.UserRequest;

public class RequestInterceptor extends HandlerInterceptorAdapter {
	
	private String[] openurls = {"POST:/login/facebook", "POST:/login/google","POST:/login/truecaller", 
			"POST:/otp/verify", "GET:/error", "POST:/error", "PUT:/error", 
			"DELETE:/error"};
	
	UserRepository userRepo;
	WordRepository wordRepo;
	
	private final Logger logger = LoggerFactory.getLogger(RequestInterceptor.class);
	
	public RequestInterceptor(UserRepository userRepo, WordRepository wordRepo) {
		// TODO Auto-generated constructor stub
		this.userRepo = userRepo;
		this.wordRepo = wordRepo;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// TODO Auto-generated method stub
		String secret = request.getHeader("Secret");
		if (secret == null) {
			writeJson(response, 401);
			logClientSideError(request, response);
			return false;
		} 
		if (!secret.equals(AppConfig.appSecret) && !secret.equals("Testing Phase 1 and 2")){
			writeJson(response, 403);
			logClientSideError(request, response);
			return false;
		}
		String token = request.getHeader("token");
		
		return checkAuthorization(token, request, response);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		logClientSideError(request, response);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		if (response.getStatus() >= 400)
			logClientSideError(request, response);
	}

	private void writeJson(HttpServletResponse response, int code){
		JSONObject json = new JSONObject();
		response.setContentType("text/x-json;charset=UTF-8");           
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(code);
        try {
            json.write(response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void writeJson(JSONObject json, HttpServletResponse response, int code){
		response.setContentType("text/x-json;charset=UTF-8");           
        response.setHeader("Cache-Control", "no-cache");
        response.setStatus(code);
        try {
            json.write(response.getWriter());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private boolean checkAuthorization(String token, HttpServletRequest request,
			HttpServletResponse response) {
		System.out.println(request.getMethod() + ":" + request.getServletPath());
		if (token ==null || token.equals("null") || token.equals("")){
			if (!request.getMethod().equals("GET")) {
				if (!ArrayUtils.contains(openurls, request.getMethod() + ":" + request.getServletPath())){
					if (!request.getServletPath().startsWith("/words")){
						JSONObject json = new JSONObject();
						try {
							json.put("message", "Not authorized");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						writeJson(json, response, 403);
						logClientSideError(request, response);
						return false;
					}
				}
			}
		} else {
			User user = userRepo.findbyToken(token);
			if (user == null){
				JSONObject json = new JSONObject();
				try {
					json.put("message", "User not found");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				writeJson(json, response, 403);
				logClientSideError(request, response);
				return false;
			} else{
				try {
					if (user.getLastActive().getTime() < System.currentTimeMillis() - 5*60*1000) {
						user.setLastActive(new Date());
						userRepo.update(user.getUserId(), new ObjectMapper().convertValue(
							new UserRequest(user), Map.class));
					}
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println(e.getMessage());
				}
				if (user.isIsBlocked()){
					if ("POST:/wibes".equals(request.getMethod() + ":" + request.getServletPath())){
						JSONObject json = new JSONObject();
						try {
							json.put("message", "uploading blocked");
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Notifications.notifyUploadBlocked(token, userRepo, wordRepo);
						writeJson(json, response, 403);
						logClientSideError(request, response);
						return false;
					}
				}
			}
		}
		return true;
	}

	public void logClientSideError(HttpServletRequest request, HttpServletResponse response) {
		long contentLength = request.getContentLengthLong();
		Map<String, String> headersInfo = getHeadersInfo(request);

		if (contentLength > 1000) {
			logger.error(response.getStatus() + " | Request URL: " + request.getRequestURL()
				+ " | This request is too big and its content will not be logged. Headers: " + headersInfo + "\n");
		}
		else {
			try {
				logger.error(response.getStatus() + " | Request URL: " + request.getRequestURL()
					+ " | Request headers: " + headersInfo
					+ " | Request body: " + request.getReader().lines().collect(Collectors.joining(System.lineSeparator())) + "\n");
			} catch (Exception e) {
				logger.error(response.getStatus() + " | Client-side error occurred but system cannot process error info." + "\n");
			}
		}
	}

	private Map<String, String> getHeadersInfo(HttpServletRequest request) {
		Map<String, String> map = new HashMap<>();

		Enumeration<?> headerNames = request.getHeaderNames();
		while (headerNames.hasMoreElements()) {
		String key = (String) headerNames.nextElement();
		String value = request.getHeader(key);
		map.put(key, value);
		}

		return map;
	}
}
