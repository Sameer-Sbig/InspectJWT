package com.sbigeneral.LoginCapatchePoc.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sbigeneral.LoginCapatchePoc.Entity.User;
import com.sbigeneral.LoginCapatchePoc.Service.AuthenticationService;


@RestController
public class AuthenticationController {
	
	private AuthenticationService authService;
	   @Autowired
	    public AuthenticationController(AuthenticationService authService) {
	        this.authService = authService;
	    }
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody User user){
		return ResponseEntity.ok(authService.register(user));
	}
	
	@PostMapping("/login1")
	public ResponseEntity<?> login(@RequestBody User user){
		System.out.println("Inside controller");
		return ResponseEntity.ok(authService.authenticate(user));
	}
	
	@PostMapping("/test")
	public ResponseEntity<?> test(){
		System.out.println("Inside test controller");
		return ResponseEntity.ok("gg");
	}

}
