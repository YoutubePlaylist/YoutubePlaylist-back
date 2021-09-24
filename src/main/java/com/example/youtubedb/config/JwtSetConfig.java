package com.example.youtubedb.config;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class JwtSetConfig {
	String secretKey;
	SignatureAlgorithm signatureAlgorithm;

	public JwtSetConfig(String secretKey) {
		this.secretKey = secretKey;
		this.signatureAlgorithm = SignatureAlgorithm.HS512;
	}
}
