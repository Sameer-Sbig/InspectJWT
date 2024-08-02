package com.sbigeneral.LoginCapatchePoc.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.sbigeneral.LoginCapatchePoc.Entity.User;


public interface UserRepository extends JpaRepository<User, Integer> {

	
	Optional<User>  findByUsername(String username);

	/*
	 * @Query("select u from User u where u.username = :username") Optional<User>
	 * findByagrementcode(String agrementcode);
	 */
	@Query("select u from User u where u.username = :username")
	public User findByUsername1(String username);
	
}
