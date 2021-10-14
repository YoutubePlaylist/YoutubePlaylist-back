package com.example.youtubedb.domain.token;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

import static com.example.youtubedb.util.ContractUtil.requires;

@Value
@Accessors(fluent = true)
public class RefreshToken {
	Instant expirationAt;

	RefreshToken(Instant expirationAt) {
		requires(expirationAt.isAfter(Instant.now()));
		this.expirationAt = expirationAt;
	}

	@RequiredArgsConstructor
	@Component
	static public class Provider {
		private final Period REFRESH_TOKEN_EXPIRE_DATE_APP = Period.ofDays(7);
		private final Period REFRESH_TOKEN_EXPIRE_DATE_PC = Period.ofDays(30);
		private final CurrentTimeServer currentTimeServer;

		public RefreshToken create(boolean isPC) {
			return new RefreshToken(
				currentTimeServer.now().truncatedTo(ChronoUnit.SECONDS).plus(getPeriodForDevice(isPC))
			);
		}

		public RefreshToken create(Instant expiration) {
			return new RefreshToken(expiration);
		}

		private TemporalAmount getPeriodForDevice(boolean isPC) {
			if(isPC) {
				return REFRESH_TOKEN_EXPIRE_DATE_PC;
			}
			return REFRESH_TOKEN_EXPIRE_DATE_APP;
		}
	}
}
