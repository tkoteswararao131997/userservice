package com.bridgelabz.userservice.serviceImpl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.client.RestTemplate;

import com.bridgelabz.userservice.dto.ForgotPwdDto;
import com.bridgelabz.userservice.dto.LoginDto;
import com.bridgelabz.userservice.dto.UpdatePwdDto;
import com.bridgelabz.userservice.dto.UserDto;
import com.bridgelabz.userservice.entity.UserEntity;
import com.bridgelabz.userservice.exception.CustomException;
import com.bridgelabz.userservice.repository.UserRepository;
import com.bridgelabz.userservice.service.UserServiceInf;
import com.bridgelabz.userservice.utility.Jms;
import com.bridgelabz.userservice.utility.JwtOperations;

@Service
@CrossOrigin
public class UserServiceImpl implements UserServiceInf {
	@Autowired
	private UserRepository userrepo;
	@Autowired
	private BCryptPasswordEncoder pwdencoder;
	@Autowired
	private JwtOperations jwt=new JwtOperations();
	@Autowired
	private Jms jms;
	@Autowired
	private RestTemplate restTemplate;
	@Override
	public UserEntity registerUser(UserDto dto)
	{
		isEmailExists(dto.getEmail());
		UserEntity entity=new UserEntity();
		BeanUtils.copyProperties(dto, entity);
		entity.setCreateDate(LocalDateTime.now());
		entity.setUpdateDate(LocalDateTime.now());
		entity.setPassword(pwdencoder.encode(entity.getPassword()));
		entity.setProfile("https://kevin5.s3.ap-south-1.amazonaws.com/commonprofile.png");
		UserEntity res =userrepo.save(entity);
		restTemplate.exchange("http://localhost:8091/user/regVerification/"+entity.getEmail(),HttpMethod.GET,null, String.class).getBody();
		return entity;
	}
	@Override
	public UserEntity loginUser(LoginDto dto) {
		UserEntity user=userrepo.getUserByEmail(dto.getEmail()).orElseThrow(() -> new CustomException("login failed",HttpStatus.NOT_FOUND,null));
		boolean ispwd=pwdencoder.matches(dto.getPassword(),user.getPassword());
		if(ispwd==false) {
			throw new CustomException("login failed",HttpStatus.NOT_ACCEPTABLE,null);
		} else {
			String token=jwt.jwtToken(user.getUserid());
			return user;
			
		}

	}
	@Override
	public List<UserEntity> getall()
	{
		return userrepo.getAllUsers().orElseThrow(() -> new CustomException("no users present", HttpStatus.NOT_FOUND,null));
	}
	@Override
	public UserEntity getUserByEmail(String email) {
		System.out.println(email);
	UserEntity user=userrepo.getUserByEmail(email).orElseThrow(() -> new CustomException("email not exists",HttpStatus.NOT_FOUND,null));
	return user;
	}
	
	@Override
	public UserEntity verify(String token) {
		Long id=jwt.parseJWT(token);
		UserEntity user=userrepo.isIdExists(id).orElseThrow(() -> new CustomException("user not exists",HttpStatus.NOT_FOUND,null));
		user.setVerifyEmail(true);
		userrepo.save(user);
		return user;
	}
	@Override
	@Cacheable(value="twenty-second-cache", key = "'tokenInCache'+#token", 
    condition = "#isCacheable != null && #isCacheable")
	public UserEntity getUserById(Long id, boolean cacheable) {
		return userrepo.getUserById(id).orElseThrow(() -> new CustomException("user not exists",HttpStatus.NOT_FOUND,null));
	}
	@Override
	public UserEntity getUserById(Long id) {
		System.out.println(id);
		return userrepo.getUserById(id).orElseThrow(() -> new CustomException("user not exists",HttpStatus.NOT_FOUND,null));
	}
	
	public UserEntity getUser(String token) {
		Long id=jwt.parseJWT(token);
		UserEntity user= userrepo.getUserById(id).orElseThrow(() -> new CustomException("user not exists",HttpStatus.NOT_FOUND,null));
		System.out.println(user);
		return user;
	}
	@Override
	public boolean isIdPresent(Long id) {
		UserEntity user=userrepo.getUserById(id).orElseThrow(() -> new CustomException("user not exists",HttpStatus.NOT_FOUND,null));
		if(user.getEmail()!=null)
		return true;
		return false;
	}
	public void deleteUser(Long userId) {
			UserEntity user=getUserById(userId);
			String token=jwt.jwtToken(userId);
			HttpHeaders headers = new HttpHeaders();
	        headers.set("token",token);
	        HttpEntity<String> entity = new HttpEntity<String>(headers);
			restTemplate.exchange("http://localhost:8092/notes/deleteallnotes",HttpMethod.DELETE, entity, Void.class);
			restTemplate.exchange("http://localhost:8092/labels/deletealllabels",HttpMethod.DELETE, entity, Void.class);
			userrepo.delete(user);
	}
	@Override
	public boolean isEmailExists(String email) {
		if(userrepo.isEmailExists(email).isPresent())
			throw new CustomException("email already exists",HttpStatus.BAD_REQUEST,null);
		return false;
	}
	@Override
	public UserEntity updatepwd(UpdatePwdDto pwddto) {
			Long userid=jwt.parseJWT(pwddto.getToken());
			UserEntity user=getUserById(userid);
			user.setPassword(pwdencoder.encode(pwddto.getNewpassword()));
			userrepo.save(user);
			return user;
	}
	@Override
	public String forgotPwd(ForgotPwdDto forgotdto) {
		UserEntity user=getUserByEmail(forgotdto.getEmail());
		restTemplate.exchange("http://localhost:8091/resetPwd/"+user.getEmail()+"/"+jwt.jwtToken(user.getUserid()),HttpMethod.GET,null, String.class).getBody();
		return "success";
	}
	public void saveUser(UserEntity user)
	{
		userrepo.save(user);
	}
	public String getIdByEmail(String email) {
		UserEntity user=getUserByEmail(email);
		return jwt.jwtToken(user.getUserid());
	}
	
}
