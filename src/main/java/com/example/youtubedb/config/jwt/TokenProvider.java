package com.example.youtubedb.config.jwt;

import com.example.youtubedb.domain.token.AccessToken;
import com.example.youtubedb.domain.token.Jwt;
import com.example.youtubedb.domain.token.RefreshToken;
import com.example.youtubedb.domain.token.Token;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static java.sql.Timestamp.valueOf;

@Slf4j
@Component
public class TokenProvider {
	private final AccessTokenProvider accessTokenProvider;
	private final RefreshToken.Provider refreshTokenProvider;
	private final RefreshTokenParser refreshTokenParser;

	@Autowired
	public TokenProvider(AccessTokenProvider accessTokenProvider,
											 RefreshToken.Provider refreshTokenProvider,
											 RefreshTokenParser refreshTokenParser) {
		this.accessTokenProvider = accessTokenProvider;
		this.refreshTokenProvider = refreshTokenProvider;
		this.refreshTokenParser = refreshTokenParser;
	}

	public Token create(String loginId, boolean isPC) {
		// Access Token 생성
		AccessToken accessToken = accessTokenProvider.create(loginId);

		// Refresh Token 생성
		RefreshToken refreshToken = refreshTokenProvider.create(isPC);

		return Jwt.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}

	public Token reissue(String loginId, String refreshTokenValue) {
		AccessToken accessToken = accessTokenProvider.create(loginId);
		RefreshToken refreshToken = refreshTokenParser.parse(refreshTokenValue);
		return Jwt.builder()
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build();
	}
}