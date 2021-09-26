package com.example.youtubedb.config;

import com.example.youtubedb.config.jwt.AccessTokenProvider;
import com.example.youtubedb.config.jwt.JwtFormatter;
import com.example.youtubedb.config.jwt.RefreshTokenProvider;
import com.example.youtubedb.config.jwt.time.RealTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TokenConfig {
	@Autowired
	private JwtSetConfigYamlAdapter jwtSetConfigYamlAdapter;

	@Bean
	public AccessTokenProvider accessTokenProvider() {
		return new AccessTokenProvider(new RealTime());
	}

	@Bean
	public RefreshTokenProvider refreshTokenProvider() {
		return new RefreshTokenProvider(new RealTime());
	}

	@Bean
	public JwtFormatter jwtFormatter() {
		return new JwtFormatter(jwtSetConfigYamlAdapter.toJwtSetConfig());
	}
}
