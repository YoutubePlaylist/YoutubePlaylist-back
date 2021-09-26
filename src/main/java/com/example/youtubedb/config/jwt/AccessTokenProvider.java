package com.example.youtubedb.config.jwt;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import com.example.youtubedb.domain.token.AccessToken;
import lombok.RequiredArgsConstructor;

import java.time.Duration;

@RequiredArgsConstructor
public class AccessTokenProvider {
	private final Duration ACCESS_TOKEN_EXPIRE_TIME = Duration.ofMinutes(30);
	private final CurrentTimeServer currentTimeServer;

	public AccessToken create(String loginId) {
		return new AccessToken(
			loginId,
			currentTimeServer.now().plus(ACCESS_TOKEN_EXPIRE_TIME)
		);
	}
}
