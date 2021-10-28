package com.example.youtubedb.config.jwt;

import com.example.youtubedb.config.JwtSetConfig;
import com.example.youtubedb.domain.token.accessToken.AccessToken;
import com.example.youtubedb.domain.token.refreshToken.RefreshToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.sql.Date;

public final class JwtFormatter {
	private final SecretKey key;

	public JwtFormatter(JwtSetConfig jwtSetConfig) {
		this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSetConfig.secretKey()));
	}

	public String toJwtFromAccessToken(AccessToken accessToken) {
		return Jwts.builder()
			.setSubject(accessToken.loginId())
			.setExpiration(Date.from(accessToken.expirationAt()))
			.signWith(key)
			.compact();
	}

	public String toJwtFromRefreshToken(RefreshToken refreshToken) {
		return Jwts.builder()
			.setExpiration(Date.from(refreshToken.expirationAt()))
			.signWith(key)
			.compact();
	}
}
