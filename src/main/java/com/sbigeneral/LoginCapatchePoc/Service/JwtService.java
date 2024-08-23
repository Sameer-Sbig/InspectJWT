package com.sbigeneral.LoginCapatchePoc.Service;
import java.util.Date;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.sbigeneral.LoginCapatchePoc.Entity.User;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	
	private final String SECRET_KEY = "8203e7b0cd3d00d0d0a50e044d9c8f81686c6914c9b4748bbe8e4cac6fa9ae45";
		
	private boolean isTokenExpired(String token) {
		return extractExpiration(token).before(new Date());
	}
	
	private Date extractExpiration(String token) {
		return extractClaim(token,Claims::getExpiration);
	}
	
	public boolean isValid(String token , UserDetails user) {
		String username = extractUsername(token);
		return (username.equals(user.getUsername())) && !isTokenExpired(token);
	}
	
	public String extractUsername(String token) {
		return extractClaim(token,Claims::getSubject);
	}
//	private Claims extractAllClaims(String token) {
//		return Jwts.parser().verifyWith(getSigninKey()).build().parseSignedClaims(token).getPayload();
//		
//	}
//	
	private Claims extractAllClaims(String token) {
	    return Jwts.parser()
	            .setSigningKey(getSigningKeyBytes())
	            .parseClaimsJws(token)
	            .getBody();
	}
//
//	private SecretKey getSigninKey() {
//	    byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
//	    return Keys.hmacShaKeyFor(keyBytes);
//	}
	public <T> T extractClaim(String token , Function<Claims , T> resolver) {
		Claims claims = extractAllClaims(token);
		return resolver.apply(claims);
	}
	

	public String generateToken(User user) {
	    String token = Jwts.builder()
	            .setSubject(user.getEmployeeId())
	            .setIssuedAt(new Date(System.currentTimeMillis()))
	            .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
	            .signWith(SignatureAlgorithm.HS256, getSigningKeyBytes())  // Use byte[] key
	            .compact();
	    return token;
	}

	private byte[] getSigningKeyBytes() {
	    return Decoders.BASE64.decode(SECRET_KEY);
	}


	
//	private SecretKey getSigninKey() {
//		byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY);
//		return Keys.hmacShaKeyFor(keyBytes);
//	}
}
