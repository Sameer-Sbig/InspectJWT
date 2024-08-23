package com.sbigeneral.LoginCapatchePoc.Utill;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.sbigeneral.LoginCapatchePoc.Service.UserDetailsServiceImpl;
import com.sbigeneral.LoginCapatchePoc.filter.JwtAuthenticationFilter;



@Configuration
@EnableWebSecurity
public class SecurityConfig {

	private UserDetailsServiceImpl userDetailsServiceImpl;
	private JwtAuthenticationFilter jwtAuthenticationFilter;

	public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl,
			JwtAuthenticationFilter jwtAuthenticationFilter) {
		this.userDetailsServiceImpl = userDetailsServiceImpl;
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(req -> req.antMatchers("/login1/**", "/register/**", "/breakin/**").permitAll()
						.anyRequest().authenticated())
				.userDetailsService(userDetailsServiceImpl)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				
				  .addFilterBefore(jwtAuthenticationFilter,
				  UsernamePasswordAuthenticationFilter.class)
				 
				.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
}




// package com.sbigeneral.LoginCapatchePoc.Utill;

// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
// import org.springframework.security.config.http.SessionCreationPolicy;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.security.web.session.HttpSessionEventPublisher;

// import com.sbigeneral.LoginCapatchePoc.Service.UserDetailsServiceImpl;
// import com.sbigeneral.LoginCapatchePoc.filter.JwtAuthenticationFilter;

// @Configuration
// @EnableWebSecurity
// public class SecurityConfig {

// 	private final UserDetailsServiceImpl userDetailsServiceImpl;
// 	private final JwtAuthenticationFilter jwtAuthenticationFilter;

// 	public SecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl,
// 			JwtAuthenticationFilter jwtAuthenticationFilter) {
// 		this.userDetailsServiceImpl = userDetailsServiceImpl;
// 		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
// 	}

// 	@Bean
// 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// 		http.csrf(AbstractHttpConfigurer::disable)
// 				.authorizeHttpRequests(req -> req.antMatchers("/login1/**", "/register/**", "/breakin/**").permitAll()
// 						.anyRequest().authenticated())
// 				.userDetailsService(userDetailsServiceImpl)
// 				.sessionManagement(session -> session
// 						.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)  // Always create a session
// 						.maximumSessions(1)  // Ensure only one session per user
// 						.maxSessionsPreventsLogin(true) // Prevents new login if session exists
// 						.expiredUrl("/session-expired"))  // Redirect to this URL when session expires
// 				.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

// 		http.sessionManagement()
// 				.sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
// 				.maximumSessions(1)
// 				.maxSessionsPreventsLogin(true)
// 				.expiredUrl("/session-expired");
		
// 		http.sessionManagement()
// 			.sessionFixation().migrateSession()
// 			.invalidSessionUrl("/session-invalid");

// 		return http.build();
// 	}

// 	@Bean
// 	public PasswordEncoder passwordEncoder() {
// 		return new BCryptPasswordEncoder();
// 	}

// 	@Bean
// 	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
// 		return configuration.getAuthenticationManager();
// 	}
	
// 	@Bean
// 	public HttpSessionEventPublisher httpSessionEventPublisher() {
// 		return new HttpSessionEventPublisher();
// 	}
// }

