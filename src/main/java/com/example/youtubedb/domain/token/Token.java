package com.example.youtubedb.domain.token;

import lombok.*;

import java.util.Date;

@Getter
public class Token {
	protected GrantType grantType;
	protected String accessToken;
	@Setter
	protected String refreshToken;
	protected Date accessTokenExpiresIn;
	protected Date refreshTokenExpiresIn;
}
