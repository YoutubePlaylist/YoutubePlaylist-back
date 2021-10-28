package com.example.youtubedb.config;

import com.example.youtubedb.config.jwt.*;
import com.example.youtubedb.config.jwt.time.RealTime;
import com.example.youtubedb.domain.token.accessToken.AccessToken;
import com.example.youtubedb.domain.token.accessToken.AccessTokenProvider;
import com.example.youtubedb.domain.token.refreshToken.RefreshToken;
import com.example.youtubedb.domain.token.refreshToken.RefreshTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TokenConfig {
	private final JwtSetConfigYamlAdapter jwtSetConfigYamlAdapter;
	private final AccessTokenProvider accessTokenProvider;
	private final RefreshTokenProvider refreshTokenProvider;

	@Bean
	public JwtFormatter jwtFormatter() {
		return new JwtFormatter(jwtSetConfigYamlAdapter.toJwtSetConfig());
	}

	@Bean
	public AccessTokenParser accessTokenParser() {
		return new AccessTokenParser(jwtSetConfigYamlAdapter.toJwtSetConfig(), accessTokenProvider);
	}

	@Bean
	public RefreshTokenParser refreshTokenParser() {
		return new RefreshTokenParser(jwtSetConfigYamlAdapter.toJwtSetConfig(), refreshTokenProvider);
	}

	@Bean
	public JwtResolver jwtResolver() {
		return new JwtResolver(jwtSetConfigYamlAdapter.toJwtSetConfig());
	}
}
