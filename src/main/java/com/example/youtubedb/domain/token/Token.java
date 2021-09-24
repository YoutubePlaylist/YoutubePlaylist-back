package com.example.youtubedb.domain.token;

import lombok.*;

import java.util.Date;

import static com.example.youtubedb.util.ContractUtil.requires;

@Getter
public class Token {
	protected GrantType grantType;
	protected AccessToken accessToken;
	@Setter
	protected String refreshToken;
	protected Date refreshTokenExpiresIn;

	public Token(AccessToken accessToken, String refreshToken, Date refreshTokenExpiresIn) {
		requires(accessToken != null);
		requires(!refreshToken.isEmpty() && refreshToken!=null);
		requires(refreshTokenExpiresIn != null);

		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.refreshTokenExpiresIn = refreshTokenExpiresIn;
	}
}
