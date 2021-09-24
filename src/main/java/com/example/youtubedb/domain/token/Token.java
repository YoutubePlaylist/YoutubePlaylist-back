package com.example.youtubedb.domain.token;

import lombok.Getter;

import static com.example.youtubedb.util.ContractUtil.requires;

@Getter
public class Token {
	protected GrantType grantType;
	protected AccessToken accessToken;
	protected RefreshToken refreshToken;

	public Token(AccessToken accessToken, RefreshToken refreshToken) {
		requires(accessToken != null);
		requires(refreshToken != null);

		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
}
