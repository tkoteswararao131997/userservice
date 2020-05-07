package com.bridgelabz.userservice.service;

import java.util.List;

import com.bridgelabz.userservice.dto.ForgotPwdDto;
import com.bridgelabz.userservice.dto.LoginDto;
import com.bridgelabz.userservice.dto.UpdatePwdDto;
import com.bridgelabz.userservice.dto.UserDto;
import com.bridgelabz.userservice.entity.UserEntity;

public interface UserServiceInf {
	UserEntity registerUser(UserDto dto);
	UserEntity getUserByEmail(String email);
	List<UserEntity> getall();
	UserEntity verify(String token);
	UserEntity getUserById(Long userId);
	boolean isIdPresent(Long id);
	boolean isEmailExists(String email);
	UserEntity loginUser(LoginDto dto);
	UserEntity updatepwd(UpdatePwdDto pwddto);
	String forgotPwd(ForgotPwdDto forgotdto);
	UserEntity getUserById(Long id, boolean cacheable);

}
