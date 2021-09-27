package com.example.youtubedb.config.jwt;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import com.example.youtubedb.domain.token.RefreshToken;
import lombok.RequiredArgsConstructor;

import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

@RequiredArgsConstructor
public class RefreshTokenProvider {
	private final Period REFRESH_TOKEN_EXPIRE_DATE_APP = Period.ofDays(7);
	private final Period REFRESH_TOKEN_EXPIRE_DATE_PC = Period.ofDays(60);
	private final CurrentTimeServer currentTimeServer;

	protected RefreshToken create(boolean isPC) {
		return new RefreshToken(
			currentTimeServer.now().truncatedTo(ChronoUnit.SECONDS).plus(getPeriodForDevice(isPC))
		);
	}

	private TemporalAmount getPeriodForDevice(boolean isPC) {
		if(isPC) {
			return REFRESH_TOKEN_EXPIRE_DATE_PC;
		}
		return REFRESH_TOKEN_EXPIRE_DATE_APP;
	}
}
