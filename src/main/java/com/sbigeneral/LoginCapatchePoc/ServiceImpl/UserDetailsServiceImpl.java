package com.sbigeneral.LoginCapatchePoc.ServiceImpl;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
	
//	
	@Override
	public UserDetails login(UserModel user) {
		// TODO Auto-generated method stub
		UserDetails checkLogin = userDetailsRepo.checkLogin(user.getEmployeeId() , user.getPassword());
		
		System.out.println("the session count is"  + checkLogin.getSessionCount());
		// new code very imp
		if(checkLogin.getSessionCount() > 0) {
			throw new IllegalStateException("User is already logged in from other session")	;
			
			
		}
		
		  checkLogin.setSessionCount(1);
		  userDetailsRepo.save(checkLogin);
		  
		  System.out.println("the session count is"  + checkLogin.getSessionCount());
		return checkLogin ;
	}
	
    public void logout(String employeeId) {
        UserDetails existingUser = userDetailsRepo.findByEmployeeId(employeeId);

        if (existingUser != null) {
            existingUser.setSessionCount(0);
            userDetailsRepo.save(existingUser);
        }
    }
	
	
	
	
	
///////////////////////////////// login page with simultaneously login api /////////////////////////////	
//	public UserDetails login(UserModel user) {
//        UserDetails existingUser = userDetailsRepo.checkLogin(user.getEmployeeId(), user.getPassword());
//        System.out.println("Existing user = " + existingUser);
//
//        if (existingUser == null) {
//            // Handle invalid credentials
//            return null;
//        }
//
//        if (existingUser.isLoggedIn()) {
//            // Handle already logged-in user
//            throw new RuntimeException("User is already logged in from another session.");
//        }
//
//        // Update login status
//        existingUser.setLoggedIn(true);
//        userDetailsRepo.save(existingUser);
//
//        return existingUser;
//    }
//
//    public void logout(String employeeId) {
//        UserDetails existingUser = userDetailsRepo.findByEmployeeId(employeeId);
//
//        if (existingUser != null) {
//            existingUser.setLoggedIn(false);
//            userDetailsRepo.save(existingUser);
//        }
//    }

    
    // new code 

}
