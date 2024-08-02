package com.sbigeneral.LoginCapatchePoc.ServiceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sbigeneral.LoginCapatchePoc.Entity.UserDetails;
import com.sbigeneral.LoginCapatchePoc.Repository.UserDetailsRepo;
import com.sbigeneral.LoginCapatchePoc.Service.UserDetailsService;
import com.sbigeneral.LoginCapatchePoc.model.UserModel;





@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	UserDetailsRepo userDetailsRepo;
	@Override
	public UserDetails getUserByEmployeeID(String employeeId) {
		
		UserDetails byEmployeeId = userDetailsRepo.findByEmployeeId(employeeId);
		
		return byEmployeeId;
	}
	@Override
	public UserDetails login(UserModel user) {
		// TODO Auto-generated method stub
		UserDetails checkLogin = userDetailsRepo.checkLogin(user.getEmployeeId() , user.getPassword());
		return checkLogin ;
	}

}
