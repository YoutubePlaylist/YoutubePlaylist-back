package com.example.youtubedb.config.jwt;

import com.example.youtubedb.config.JwtSetConfig;
import com.example.youtubedb.domain.token.accessToken.AccessToken;
import com.example.youtubedb.domain.token.accessToken.AccessTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class AccessTokenParser implements TokenParser<AccessToken> {
	private final JwtParser parser;
	private final AccessTokenProvider provider;

	public AccessTokenParser(JwtSetConfig jwtSetConfig, AccessTokenProvider provider) {
		this.parser = Jwts.parserBuilder()
			.setSigningKey(jwtSetConfig.secretKey())
			.build();
		this.provider = provider;
	}

	@Override
	public AccessToken parse(String tokenString) {
		final Jws<Claims> claimsJws = parser.parseClaimsJws(tokenString);
		final Claims claims = claimsJws.getBody();
		final String loginId = claims.getSubject();
		final Instant expiration = claims
			.getExpiration()
			.toInstant();

		return provider.create(loginId, expiration);
	}
}
