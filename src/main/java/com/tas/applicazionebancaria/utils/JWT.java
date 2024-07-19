package com.tas.applicazionebancaria.utils;

import java.security.Key;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import javax.crypto.spec.SecretKeySpec;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JWT implements Costanti {
	
	public static String generate(String nome, String cognome, String email) {
		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET), SignatureAlgorithm.HS256.getJcaName());
		Instant now = Instant.now();
		String jwtToken = Jwts.builder()
				.claim("nome", nome)
				.claim("cognome", cognome)
				.setSubject(email)
				.setId(UUID.randomUUID().toString())
				.setIssuedAt(Date.from(now))
				.setExpiration(Date.from(now.plus(24l, ChronoUnit.HOURS)))
				.signWith(hmacKey)
				.compact();
		return jwtToken;
	}
	
	public static Jws<Claims> validate(String jwtToken) {
		Key hmacKey = new SecretKeySpec(Base64.getDecoder().decode(SECRET), SignatureAlgorithm.HS256.getJcaName());
		
		Jws<Claims> claims = Jwts.parserBuilder()
				.setSigningKey(hmacKey)
				.build()
				.parseClaimsJws(jwtToken);
		
		return claims;
	}
}