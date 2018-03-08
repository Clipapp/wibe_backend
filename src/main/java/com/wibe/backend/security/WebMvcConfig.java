package com.wibe.backend.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.wibe.backend.repositories.UserRepository;
import com.wibe.backend.repositories.WordRepository;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {

@Autowired
UserRepository userRepo;

@Autowired
WordRepository wordRepo;
	
@Override
public void addInterceptors(InterceptorRegistry registry) {
	// TODO Auto-generated method stub
	registry.addInterceptor(new RequestInterceptor(userRepo, wordRepo));
}

  
}