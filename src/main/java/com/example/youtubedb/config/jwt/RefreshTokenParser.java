package com.example.youtubedb.config.jwt;

import com.example.youtubedb.config.JwtSetConfig;
import com.example.youtubedb.domain.token.RefreshToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;

public class RefreshTokenParser implements TokenParser<RefreshToken> {
	private final JwtParser parser;
	private final RefreshToken.Provider provider;

	public RefreshTokenParser(JwtSetConfig jwtSetConfig, RefreshToken.Provider provider) {
		this.parser = Jwts.parserBuilder()
			.setSigningKey(jwtSetConfig.secretKey())
			.build();
		this.provider = provider;
	}

	@Override
	public RefreshToken parse(String tokenString) {
		final Jws<Claims> claimsJws = parser.parseClaimsJws(tokenString);
		final Claims claims = claimsJws.getBody();
		final Instant expiration = claims
			.getExpiration()
			.toInstant();

		return provider.create(expiration);
	}
}
