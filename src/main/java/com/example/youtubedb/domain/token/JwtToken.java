package com.example.youtubedb.domain.token;

import lombok.*;

import java.util.Date;

public class JwtToken extends Token {
	@Builder
	public JwtToken(AccessToken accessToken, String refreshToken, Date refreshTokenExpiresIn) {
		super(accessToken, refreshToken, refreshTokenExpiresIn);
		this.grantType = GrantType.BEARER;
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}
}
