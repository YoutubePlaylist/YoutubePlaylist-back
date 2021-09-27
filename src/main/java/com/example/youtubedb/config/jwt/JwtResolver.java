package com.example.youtubedb.config.jwt;

import com.example.youtubedb.config.JwtSetConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import javax.crypto.SecretKey;

public class JwtResolver {
	private final SecretKey key;

	public JwtResolver(JwtSetConfig jwtSetConfig) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSetConfig.secretKey()));
	}

	public Authentication getAuthentication(String accessToken) {
		Claims claims = parseClaims(accessToken);

		UserDetails principal = new User(claims.getSubject(), "", null);

		return new UsernamePasswordAuthenticationToken(principal, "", null);
	}

	public boolean validateToken(String token) throws Exception {
		Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
		return true;
	}

	private Claims parseClaims(String accessToken) throws ExpiredJwtException {
		return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
	}
}
