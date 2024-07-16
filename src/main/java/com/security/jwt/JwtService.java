package com.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
	// HeLYu9+3Ih8paaWT0+aDo/p+jrl8d4cLjhNXExAf8TO7T82DzjCbuaHpdP6N3Vhs
	public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaim(token);
		return claimsResolver.apply(claims);
	}

	private Key getSignInKey() {
		byte[] KeysByte = Decoders.BASE64.decode(SECRET_KEYS);
		return Keys.hmacShaKeyFor(KeysByte);
	}
//
//	private final static String SECRET_KEYS = "HeLYu9+3Ih8paaWT0+aDo/p+jrl8d4cLjhNXExAf8TO7T82DzjCbuaHpdP6N3Vhs";
	private final static String SECRET_KEYS ="3961beaeb99487912c186cb9fd3e15da4e4b0f49bcd4a02764c8f68c6d58fe9f";
	public String extractUsername(String token) {

		return extractClaims(token, Claims::getSubject);

	}

	public String generateTokens(Map<String, Object> extraClaims, UserDetails userDetails) {

		return Jwts.builder().setClaims(extraClaims).setSubject(userDetails.getUsername())
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24))
				.signWith(getSignInKey(), SignatureAlgorithm.HS256).compact();

	}

	public String generateToken(UserDetails userDetail) {
		return generateTokens(new HashMap<>(), userDetail);

	}

	public Boolean isValid(String token, UserDetails userDetail) {
		final String username = extractUsername(token);
		return (username.equals(userDetail.getUsername()) && !isTokenExpired(token));

	}

	private boolean isTokenExpired(String token) {

		return extractExpiration(token).before(new Date());
	}

	private Date extractExpiration(String token) {

		return extractClaims(token, Claims::getExpiration);
	}

	private Claims getAllClaim(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();

	}

}
