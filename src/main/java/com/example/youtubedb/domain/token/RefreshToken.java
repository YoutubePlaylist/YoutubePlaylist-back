package com.example.youtubedb.domain.token;

import lombok.Value;
import lombok.experimental.Accessors;

import java.time.Instant;

import static com.example.youtubedb.util.ContractUtil.requires;

@Value
@Accessors(fluent = true)
public class RefreshToken {
	Instant expirationAt;

	public RefreshToken(Instant expirationAt) {
		requires(expirationAt.isAfter(Instant.now()));
		this.expirationAt = expirationAt;
	}
}
