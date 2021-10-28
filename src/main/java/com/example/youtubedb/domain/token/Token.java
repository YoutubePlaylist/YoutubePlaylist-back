package com.example.youtubedb.domain.token;

import com.example.youtubedb.domain.token.accessToken.AccessToken;
import com.example.youtubedb.domain.token.refreshToken.RefreshToken;
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
