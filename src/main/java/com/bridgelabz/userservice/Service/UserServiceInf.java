package com.bridgelabz.userservice.Service;

import java.util.List;

import com.bridgelabz.userservice.Dto.ForgotPwdDto;
import com.bridgelabz.userservice.Dto.LoginDto;
import com.bridgelabz.userservice.Dto.UpdatePwdDto;
import com.bridgelabz.userservice.Dto.UserDto;
import com.bridgelabz.userservice.Entity.UserEntity;

public interface UserServiceInf {
	UserEntity registerUser(UserDto dto);
	UserEntity getUserByEmail(String email);
	List<UserEntity> getall();
	UserEntity verify(String token);
	UserEntity getUserById(long userId);
	boolean isIdPresent(long id);
	boolean isEmailExists(String email);
	UserEntity loginUser(LoginDto dto);
	UserEntity updatepwd(UpdatePwdDto pwddto);
	String forgotPwd(ForgotPwdDto forgotdto);
	UserEntity getUserById(long id, boolean cacheable);

}
