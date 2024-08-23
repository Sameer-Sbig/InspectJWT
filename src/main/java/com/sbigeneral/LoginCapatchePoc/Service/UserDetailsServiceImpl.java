package com.sbigeneral.LoginCapatchePoc.Service;



import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.sbigeneral.LoginCapatchePoc.Repository.UserRepository;



@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	
	private final UserRepository repository;
	

	public UserDetailsServiceImpl(UserRepository repository) {
		super();
		this.repository = repository;
	}


	@Override
	public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return repository.findByEmployeeId(employeeId).orElseThrow(()-> new UsernameNotFoundException("User not found"));
	}

}


// import org.springframework.security.core.userdetails.UserDetails;
// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.core.userdetails.UsernameNotFoundException;
// import org.springframework.stereotype.Service;

// import com.sbigeneral.LoginCapatchePoc.Repository.UserRepository;
// import com.sbigeneral.LoginCapatchePoc.Util.TokenStore;

// @Service
// public class UserDetailsServiceImpl implements UserDetailsService {

//     private final UserRepository repository;
//     private final TokenStore tokenStore;
//     private final JwtService jwtService;

//     public UserDetailsServiceImpl(UserRepository repository, TokenStore tokenStore, JwtService jwtService) {
//         this.repository = repository;
//         this.tokenStore = tokenStore;
//         this.jwtService = jwtService;
//     }

//     @Override
//     public UserDetails loadUserByUsername(String employeeId) throws UsernameNotFoundException {
//         return repository.findByEmployeeId(employeeId)
//                 .orElseThrow(() -> new UsernameNotFoundException("User not found"));
//     }

//     public String login(String employeeId, String rawPassword) {
//         UserDetails userDetails = loadUserByUsername(employeeId);

//         // Add your password verification logic here
//         // If password verification fails, throw an exception

//         // Invalidate the existing token if one exists
//         String existingToken = tokenStore.getTokenForUser(employeeId);
//         if (existingToken != null) {
//             tokenStore.invalidateToken(employeeId);
//         }

//         // Generate a new JWT token
//         String newToken = jwtService.generateToken(user);

//         // Store the new token
//         tokenStore.storeToken(employeeId, newToken);

//         return newToken;
//     }
// }

