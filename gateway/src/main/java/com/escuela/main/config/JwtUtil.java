package com.escuela.main.config;

import java.security.Key;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

	@Value("${jwt.secret}")
	private String secret;

	private Key key() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}

	// Lanza JwtException si la firma es inválida o el token venció
	public Claims validate(String token) {
		return Jwts.parserBuilder().setSigningKey(key()).build().parseClaimsJws(token).getBody();
	}
}