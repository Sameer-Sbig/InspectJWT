package com.sbigeneral.LoginCapatchePoc.Service;

import org.springframework.stereotype.Service;

import com.sbigeneral.LoginCapatchePoc.Entity.UserDetails;
import com.sbigeneral.LoginCapatchePoc.model.UserModel;





@Service
public interface UserDetailsService {
	
	public UserDetails getUserByEmployeeID(String employeeId);
	
	public UserDetails login(UserModel user);

	public void logout(String employeeId);

}
