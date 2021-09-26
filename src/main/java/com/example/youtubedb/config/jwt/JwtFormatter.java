package com.example.youtubedb.config.jwt;

import com.example.youtubedb.config.JwtSetConfig;
import com.example.youtubedb.domain.token.AccessToken;
import com.example.youtubedb.domain.token.RefreshToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.sql.Timestamp;

public final class JwtFormatter {
	private final JwtSetConfig jwtSetConfig;
	private final SecretKey key;

	public JwtFormatter(JwtSetConfig jwtSetConfig) {
		this.jwtSetConfig = jwtSetConfig;
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSetConfig.secretKey()));
	}

	public String toJwtFromAccessToken(AccessToken accessToken) {
		return Jwts.builder()
			.setSubject(accessToken.loginId())
			.setExpiration(Timestamp.valueOf(accessToken.expirationAt().toString()))
			.signWith(key)
			.compact();
	}

	public String toJwtFromRefreshToken(RefreshToken refreshToken) {
		return Jwts.builder()
			.setExpiration(Timestamp.valueOf(refreshToken.expirationAt().toString()))
			.signWith(key)
			.compact();
	}
}
