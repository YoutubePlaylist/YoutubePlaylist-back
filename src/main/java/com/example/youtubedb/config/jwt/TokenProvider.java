package com.example.youtubedb.config.jwt;

import com.example.youtubedb.domain.token.AccessToken;
import com.example.youtubedb.domain.token.Jwt;
import com.example.youtubedb.domain.token.RefreshToken;
import com.example.youtubedb.domain.token.Token;
import com.example.youtubedb.exception.NotExistAuthorityException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import static java.sql.Timestamp.valueOf;

@Slf4j
@Component
public class TokenProvider {
	private final AccessTokenProvider accessTokenProvider;
	private final RefreshTokenProvider refreshTokenProvider;

	@Autowired
	public TokenProvider(AccessTokenProvider accessTokenProvider,
	                     RefreshTokenProvider refreshTokenProvider) {
		this.accessTokenProvider = accessTokenProvider;
		this.refreshTokenProvider = refreshTokenProvider;
	}

	public Token create(Authentication authentication, boolean isPC) {
		// Access Token 생성
		AccessToken accessToken = accessTokenProvider.create(authentication.getName());

		// Refresh Token 생성
		RefreshToken refreshToken = refreshTokenProvider.create(isPC);

		return Jwt.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}