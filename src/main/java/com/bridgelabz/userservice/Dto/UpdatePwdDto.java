package com.bridgelabz.userservice.Dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UpdatePwdDto {
	@NotBlank
	@Size(min=6,max=10)
	private String newpassword;
	@NotBlank
	@Size(min=6,max=10)
	private String conformpassword;
	private String token;
	

}
