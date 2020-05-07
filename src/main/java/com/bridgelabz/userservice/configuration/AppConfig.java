package com.bridgelabz.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.userservice.entity.UserEntity;
import com.bridgelabz.userservice.utility.JwtOperations;

@Configuration
public class AppConfig {
	@Bean
	public BCryptPasswordEncoder bcryptpasswordencoder()
	{
		return new BCryptPasswordEncoder();
	}
	@Bean
	public JwtOperations jwtoperations()
	{
		return new JwtOperations();
	}
	@Bean
	public UserEntity userentity()
	{
		return new UserEntity();
	}
	@Bean
	public RestTemplate getRestTemplate()
	{
		return new RestTemplate();
	}
}
