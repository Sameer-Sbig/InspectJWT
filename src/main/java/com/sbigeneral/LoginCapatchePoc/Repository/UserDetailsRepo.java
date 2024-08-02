package com.sbigeneral.LoginCapatchePoc.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.sbigeneral.LoginCapatchePoc.Entity.UserDetails;





@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetails,Integer > {
	
	
	@Query("SELECT u FROM UserDetails u where u.employeeId =:employeeIdInput")
	public UserDetails findByEmployeeId(@Param("employeeIdInput") String employeeIdInput);
	
	/*
	 * @Query("SELECT u FROM UserDetails u where u.employeeId =:userModel.employeeId AND u.password =:userModel.password"
	 * ) public UserDetails checkLogin(@Param("userModel") UserModel userEntered);
	 */

	@Query("SELECT u FROM UserDetails u WHERE u.employeeId = :employeeId AND u.password = :password")
	public UserDetails checkLogin(@Param("employeeId") String employeeId, @Param("password") String password);

	 
}
