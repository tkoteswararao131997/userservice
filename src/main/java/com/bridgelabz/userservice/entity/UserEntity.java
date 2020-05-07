package com.bridgelabz.userservice.entity;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;
@Data
@Entity
@Table(name="users")
public class UserEntity implements Serializable{
    public UserEntity(String string, int i) {
		this.name=string;
		this.userid=(long) i;
	}

	public UserEntity() {}

	private static final long serialVersionUID = 7156526077883281623L;
    
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userid;
	private String name;
	private String password;
	private String mobileNumber;
	private String email;
	private LocalDateTime createDate;
	private LocalDateTime updateDate;
	private boolean isVerifyEmail=false;
	private String profile;
}