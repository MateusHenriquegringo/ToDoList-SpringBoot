package com.mateus.demo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Component
public class JWTUtil {

	@Value("${jwt.secret}")
	private String secret;

	@Value("${jwt.expiration}")
	private String expiration;

	public String generateToken(String username) {
		SecretKey key = this.getKeyBySecret();

		return Jwts.builder()
				.setSubject(username)
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(this.expiration)))
				.signWith(key)
				.compact();
	}

	private SecretKey getKeyBySecret() {
		return Keys.hmacShaKeyFor(this.secret.getBytes());
	}

	public boolean isValidToken(String token) {
		Claims claims = this.getClaims(token);
		if (Objects.nonNull(claims)) {
			String username = claims.getSubject();
			Date expirationDate = claims.getExpiration();
			Date now = new Date(System.currentTimeMillis());

			return Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate);

		}
		return false;
	}


	private Claims getClaims(String token){
		SecretKey key = getKeyBySecret();
		try {
			return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJwt(token).getBody();
		} catch (Exception e){
			return null;
		}
	}
}
