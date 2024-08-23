package com.sbigeneral.LoginCapatchePoc.Service;
import java.util.Optional;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sbigeneral.LoginCapatchePoc.Entity.AuthenticationResponse;
import com.sbigeneral.LoginCapatchePoc.Entity.User;
import com.sbigeneral.LoginCapatchePoc.Repository.UserRepository;



@Service
public class AuthenticationService {
	
	private UserRepository userRepository;
	
	private PasswordEncoder passwordEncoder;
	
	private JwtService jwtService;
	
	private AuthenticationManager authenticationManager;
	
	   @Autowired
	    public AuthenticationService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
	        this.userRepository = userRepository;
	        this.passwordEncoder = passwordEncoder;
	        this.jwtService = jwtService;
	        this.authenticationManager = authenticationManager;
	    }

	public AuthenticationResponse register(User request) {
		User user = new User();
		user.setEmployeeId(request.getEmployeeId());
		user.setEmailId(request.getEmailId());
		user.setName(request.getName());
		
		//setting hashed password
		
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		
		
		
		user= userRepository.save(user);
		
		String token = jwtService.generateToken(user);
		
		return new AuthenticationResponse(token);
	}
	
//	public AuthenticationResponse authenticate(User request) {
//		authenticationManager.authenticate(
//				new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
//		User user = userRepository.findByUserName(request.getUsername()).orElseThrow();
//		String token = jwtService.generateToken(user);
//		
//		return new AuthenticationResponse(token);
//	}
	
	public AuthenticationResponse authenticate(User request) {
		
	    User user = userRepository.findByEmployeeId(request.getEmployeeId())
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    System.out.println(!passwordEncoder.matches(request.getPassword(), user.getPassword()));
	    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
	    String token = jwtService.generateToken(user);
	    System.out.println(token);

	    return new AuthenticationResponse(token);
	}


}
