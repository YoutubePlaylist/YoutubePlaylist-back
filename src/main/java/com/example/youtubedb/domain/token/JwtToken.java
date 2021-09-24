package com.example.youtubedb.domain.token;

import lombok.*;

import java.util.Date;

public class JwtToken extends Token {
	@Builder
	public JwtToken(String accessToken, String refreshToken, Date accessTokenExpiresIn, Date refreshTokenExpiresIn) {
		this.grantType = GrantType.BEARER;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.accessTokenExpiresIn = accessTokenExpiresIn;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}
}
