package com.example.youtubedb.config;

import com.example.youtubedb.config.jwt.*;
import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import com.example.youtubedb.config.jwt.time.RealTime;
import com.example.youtubedb.domain.token.RefreshToken;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class TokenConfig {
	private final JwtSetConfigYamlAdapter jwtSetConfigYamlAdapter;

	@Bean
	public AccessTokenProvider accessTokenProvider() {
		return new AccessTokenProvider(new RealTime());
	}

//	@Bean
//	public RefreshTokenProvider refreshTokenProvider() {
//		return new RefreshTokenProvider(new RealTime());
//	}

	@Bean
	public JwtFormatter jwtFormatter() {
		return new JwtFormatter(jwtSetConfigYamlAdapter.toJwtSetConfig());
	}

	@Bean
	public AccessTokenParser accessTokenParser() {
		return new AccessTokenParser(jwtSetConfigYamlAdapter.toJwtSetConfig());
	}

	@Bean
	public RefreshTokenParser refreshTokenParser() {
		return new RefreshTokenParser(jwtSetConfigYamlAdapter.toJwtSetConfig(), refreshTokenProvider); // TODO: 해결 필요
	}

	@Bean
	public JwtResolver jwtResolver() {
		return new JwtResolver(jwtSetConfigYamlAdapter.toJwtSetConfig());
	}

	@Bean
	public RefreshToken.Mapping mapping() {
		return new RefreshToken.Mapping(new RefreshToken.Pc(new RealTime()), new RefreshToken.App(new RealTime()));
	}
}
