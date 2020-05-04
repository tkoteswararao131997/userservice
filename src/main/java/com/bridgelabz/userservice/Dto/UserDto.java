package com.bridgelabz.userservice.Dto;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserDto {
	@NotBlank
	@Size(min = 3,max=10)
	private String name;
	@NotBlank
	@NotBlank
	@Size(min = 6,max=10)
	private String password;
	@Column(length = 10)
	private String mobileNumber;
	@NotBlank
	@Email
	private String email;
	public String getName() {
		return name;
	}
}
