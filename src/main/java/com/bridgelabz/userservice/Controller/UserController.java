package com.bridgelabz.userservice.Controller;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.userservice.Dto.ForgotPwdDto;
import com.bridgelabz.userservice.Dto.LoginDto;
import com.bridgelabz.userservice.Dto.UpdatePwdDto;
import com.bridgelabz.userservice.Dto.UserDto;
import com.bridgelabz.userservice.Entity.UserEntity;
import com.bridgelabz.userservice.Response.Response;
import com.bridgelabz.userservice.ServiceImpl.UserServiceImpl;
import com.bridgelabz.userservice.Utility.JwtOperations;

@RestController
@CrossOrigin("*") 
public class UserController {
	@Autowired
	private UserServiceImpl userimpl;
//	@Autowired
//    private AmazonS3ClientService amazonS3ClientService;
	@Autowired
	private JwtOperations jwt;
	/**
	 * Register User : used to register the user
	 * @param dto
	 * @return register response
	 */
	
	@PostMapping("/registeruser")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Response> registerUser(@RequestBody UserDto dto,BindingResult result)
	{

			if(result.hasErrors())
			return new ResponseEntity<Response>(new Response("invalid details",null,400,"true"),HttpStatus.BAD_REQUEST);
			UserEntity user=userimpl.registerUser(dto);
			return new ResponseEntity<Response>(new Response("regitration sucess",user,201,"true"),HttpStatus.CREATED);		
	}
	/**
	 * Login User:used to login the user
	 * @param email,password
	 * @return login response
	 */
	//@Cacheable(value = "token", key = "#token")
	@PostMapping("/loginuser")
	public ResponseEntity<Response> loginUser(@RequestBody LoginDto dto,BindingResult result)
	{
		System.out.println("hitting");
		if(result.hasErrors())
		return new ResponseEntity<Response>(new Response("invalid details",null,400,"true"),HttpStatus.BAD_REQUEST);
		UserEntity user=userimpl.loginUser(dto);
		String token =jwt.jwtToken(user.getUserid());
		System.out.println(token);
		return new ResponseEntity<Response>(new Response(token,userimpl.loginUser(dto),200,"true"),HttpStatus.OK);
	}
	/**
	 * Get All Users: used to display all the users in the table
	 * @return list of users
	 */
	@GetMapping("/getallusers")
	public ResponseEntity<Response> getAllUsers()
	{
		List<UserEntity> users=userimpl.getall();
		return new ResponseEntity<Response>(new Response("users are",users,200,"true"),HttpStatus.OK);
	}
	/**
	 * Verify Eamil : used to verify the email whether sent link is correct or not
	 * @param token
	 * @return verification response
	 */
	@GetMapping("/verifyemail/{token}")
	public ResponseEntity<Response> verifyemail(@PathVariable("token") String token)
	{
		return new ResponseEntity<Response>(new Response("email verified",userimpl.verify(token),201,"true"),HttpStatus.ACCEPTED);
	}
	/**
	 * Delete User: used to delete the present user
	 * @param userId
	 * @return response of deleted or not
	 */
	@DeleteMapping("/deleteuser/{userId}")
	public ResponseEntity<Response> deleteUser(@PathVariable("userId") long userId)
	{
			userimpl.deleteUser(userId);
			return new ResponseEntity<Response>(new Response("user deleted",null,200,"true"),HttpStatus.OK);
	}
	/**
	 * Get User By id : get the user based upon user id in the table
	 * @param userId
	 * @return user
	 */
	//@Cacheable(value = "userId", key = "#userId")
	@GetMapping("/getuserbyid/{userId}")
	public ResponseEntity<Response> getuserById(@PathVariable("userId") long userId,boolean cacheable)
	{
		//System.out.println("Getting user with ID {}."+userId);
		return new ResponseEntity<Response>(new Response("welcome",userimpl.getUserById(userId,cacheable),200,"true"),HttpStatus.OK);
	}
	
	@GetMapping("/getuser")
	public ResponseEntity<Response> getuser(@RequestHeader String token)
	{
		 Long id=jwt.parseJWT(token);
	     UserEntity user=userimpl.getUserById(id);
		return new ResponseEntity<Response>(new Response("welcome",userimpl.getUser(token),200,"true"),HttpStatus.OK);
	}
	/**
	 * Update Password : set new password for user
	 * @param pwddto
	 * @return response od update password
	 */
	@PostMapping("/updatepassword")
	public ResponseEntity<Response> updatePassword(@RequestBody UpdatePwdDto pwddto,BindingResult result)
	{
		return new ResponseEntity<Response>(new Response("password updated successfully", userimpl.updatepwd(pwddto),200,"true"),HttpStatus.OK);
	}
	@PostMapping("/forgotpassword")
	public ResponseEntity<Response> forgotPassword(@RequestBody ForgotPwdDto forgotdto,BindingResult result)
	{

		if(result.hasErrors())
		return new ResponseEntity<Response>(new Response("invalid details",null,400,"true"),HttpStatus.BAD_REQUEST);
		return new ResponseEntity<Response>(new Response("password updated and sent to mail successfully","your new pwd is:"+userimpl.forgotPwd(forgotdto),200,"true"),HttpStatus.OK);
	}
	
//	@PostMapping("/uploadProfile")
//    public ResponseEntity<Response> uploadFile(@RequestPart(value = "file") MultipartFile file,@RequestHeader String token)
//    {
//        this.amazonS3ClientService.uploadFileToS3Bucket(file, true,token);
//        Long id=jwt.parseJWT(token);
//        UserEntity user=userimpl.getUserById(id);
//        Map<String, String> response = new HashMap<>();
//        //response.put(user, "file [" + file.getOriginalFilename() + "] uploading request submitted successfully.");
//
//		return new ResponseEntity<Response>(new Response("file [" + file.getOriginalFilename() + "] uploading request submitted successfully.", user,200,"true"),HttpStatus.OK);
//
//    }
//
//    @DeleteMapping
//    public Map<String, String> deleteFile(@RequestParam("file_name") String fileName)
//    {
//        this.amazonS3ClientService.deleteFileFromS3Bucket(fileName);
//
//        Map<String, String> response = new HashMap<>();
//        response.put("message", "file [" + fileName + "] removing request submitted successfully.");
//
//        return response;
//    }
//	
}
