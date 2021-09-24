package com.example.youtubedb.domain.token;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.LocalDateTime;

import static com.example.youtubedb.util.ContractUtil.requires;

@Value
@Accessors(fluent = true)
public class AccessToken {
	String loginId;
	Instant expirationAt;
	SignatureAlgorithm signatureAlgorithm;

	public AccessToken(
		String loginId,
		Instant expirationAt,
		SignatureAlgorithm signatureAlgorithm) {

		requires(loginId.length() > 0);
		requires(expirationAt.isAfter(Instant.now()));
		requires(signatureAlgorithm != SignatureAlgorithm.NONE);

		this.loginId = loginId;
		this.expirationAt = expirationAt;
		this.signatureAlgorithm = signatureAlgorithm;
	}
}
