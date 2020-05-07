package com.bridgelabz.userservice.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.userservice.dto.ForgotPwdDto;
import com.bridgelabz.userservice.dto.LoginDto;
import com.bridgelabz.userservice.dto.UpdatePwdDto;
import com.bridgelabz.userservice.dto.UserDto;
import com.bridgelabz.userservice.entity.UserEntity;
import com.bridgelabz.userservice.response.Response;
import com.bridgelabz.userservice.serviceImpl.UserServiceImpl;
import com.bridgelabz.userservice.utility.JwtOperations;

@RestController
@CrossOrigin("*") 
public class UserController {
	@Autowired
	private UserServiceImpl userimpl;
	@Autowired
	private JwtOperations jwt;
	/**
	 * Register User : used to register the user
	 * @param dto
	 * @return register response
	 */
	
	@PostMapping("/user/registeruser")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Response> registerUser(@RequestBody UserDto dto,BindingResult result)
	{

			if(result.hasErrors())
			return new ResponseEntity<Response>(new Response("invalid details",null,400),HttpStatus.BAD_REQUEST);
			UserEntity user=userimpl.registerUser(dto);
			return new ResponseEntity<Response>(new Response("regitration sucess",user,201),HttpStatus.CREATED);		
	}
	/**
	 * Login User:used to login the user
	 * @param email,password
	 * @return login response
	 */
	//@Cacheable(value = "token", key = "#token")
	@PostMapping("/user/loginuser")
	public ResponseEntity<Response> loginUser(@RequestBody LoginDto dto,BindingResult result)
	{
		if(result.hasErrors())
		return new ResponseEntity<Response>(new Response("invalid details",null,400),HttpStatus.BAD_REQUEST);
		UserEntity user=userimpl.loginUser(dto);
		return new ResponseEntity<Response>(new Response("login success",userimpl.loginUser(dto),200),HttpStatus.OK);
	}
	/**
	 * Get All Users: used to display all the users in the table
	 * @return list of users
	 */
	@GetMapping("/user/getallusers")
	public ResponseEntity<Response> getAllUsers()
	{
		List<UserEntity> users=userimpl.getall();
		return new ResponseEntity<Response>(new Response("users are",users,200),HttpStatus.OK);
	}
	/**
	 * Verify Eamil : used to verify the email whether sent link is correct or not
	 * @param token
	 * @return verification response
	 */
	@GetMapping("/user/verifyemail/{token}")
	public ResponseEntity<Response> verifyemail(@PathVariable("token") String token)
	{
		UserEntity user=userimpl.verify(token);
		return new ResponseEntity<Response>(new Response("email verified",user,201),HttpStatus.ACCEPTED);
	}
	/**
	 * Delete User: used to delete the present user
	 * @param userId
	 * @return response of deleted or not
	 */
	@DeleteMapping("/user/deleteuser/{userId}")
	public ResponseEntity<Response> deleteUser(@PathVariable("userId") Long userId)
	{
			userimpl.deleteUser(userId);
			return new ResponseEntity<Response>(new Response("user deleted",null,200),HttpStatus.OK);
	}
	/**
	 * Get User By id : get the user based upon user id in the table
	 * @param userId
	 * @return user
	 */
	@GetMapping("/user/getuserbyid/{userId}")
	public ResponseEntity<Response> getuserById(@PathVariable("userId") Long userId)
	{
		
		return new ResponseEntity<Response>(new Response("welcome",userimpl.getUserById(userId),200),HttpStatus.OK);
	}
	@GetMapping("/user/getuser")
	public ResponseEntity<Response> getuser(@RequestHeader String token)
	{
		 Long id=jwt.parseJWT(token);
	     UserEntity user=userimpl.getUserById(id);
		return new ResponseEntity<Response>(new Response("welcome",userimpl.getUser(token),200),HttpStatus.OK);
	}
	/**
	 * Update Password : set new password for user
	 * @param pwddto
	 * @return response of update password
	 */
	@PostMapping("/user/updatepassword")
	public ResponseEntity<Response> updatePassword(@RequestBody UpdatePwdDto pwddto,BindingResult result)
	{
		UserEntity user= userimpl.updatepwd(pwddto);
		return new ResponseEntity<Response>(new Response("password updated successfully",user,200),HttpStatus.OK);
	}
	@PostMapping("/user/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestBody ForgotPwdDto forgotdto,BindingResult result)
	{

		if(result.hasErrors())
		return new ResponseEntity<Response>(new Response("invalid details",null,400),HttpStatus.BAD_REQUEST);
		userimpl.forgotPwd(forgotdto);
		return new ResponseEntity<Response>(new Response("verification email sent",null,200),HttpStatus.OK);
	}
	@GetMapping("/user/gettokenbyemail/{email}")
	public String getIdByEmail(@PathVariable("email") String email)
	{
		return userimpl.getIdByEmail(email);
	}
	@GetMapping("/user/getuser/{token}")
	public UserEntity getuserByToken(@PathVariable("token") String token)
	{
		 Long id=jwt.parseJWT(token);
	     UserEntity user=userimpl.getUserById(id);
		return userimpl.getUser(token);
	}
}
