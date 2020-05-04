package com.bridgelabz.userservice.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bridgelabz.userservice.Entity.UserEntity;
import com.bridgelabz.userservice.Utility.JwtOperations;

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
}
