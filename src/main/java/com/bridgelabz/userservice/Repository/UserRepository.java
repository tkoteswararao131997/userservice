package com.bridgelabz.userservice.Repository;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.userservice.Entity.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,String> {
	
	@Query(value="select * from users",nativeQuery=true)
	Optional<List<UserEntity>> getAllUsers();
	
	@Query(value = "select * from users where email=?1",nativeQuery = true)
	Optional<UserEntity> getUserByEmail(String email);
	
	@Query(value = "select * from users where userid=?1",nativeQuery = true)
	Optional<UserEntity> getUserById(long id);
	
	@Modifying
	@Transactional
	@Query(value = "delete users,notes from users inner join notes on notes.userid=users.userid where users.userid=?1",nativeQuery = true)
	void deleteUser(@RequestParam long userId);
	
	@Query(value = "select email from users where email=?1",nativeQuery = true)
	Optional<String> isEmailExists(String email);
	
	@Query(value = "select * from users where userid=?1",nativeQuery = true)
	Optional<UserEntity> isIdExists(long id);
	
	//users,notes from users  inner join notes on notes.userid=users.userid where users.userid=?1
	
}
