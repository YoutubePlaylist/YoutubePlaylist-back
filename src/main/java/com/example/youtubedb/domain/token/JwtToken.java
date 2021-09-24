package com.example.youtubedb.domain.token;

import lombok.*;

import java.util.Date;

public class JwtToken extends Token {
	@Builder
	public JwtToken(AccessToken accessToken, RefreshToken refreshToken) {
		super(accessToken, refreshToken);
		this.grantType = GrantType.BEARER;
	}
}
