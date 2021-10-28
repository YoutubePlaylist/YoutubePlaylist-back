package com.example.youtubedb.domain.token;

import com.example.youtubedb.domain.token.accessToken.AccessToken;
import com.example.youtubedb.domain.token.refreshToken.RefreshToken;
import lombok.Builder;

public class Jwt extends Token {
	@Builder
	public Jwt(AccessToken accessToken, RefreshToken refreshToken) {
		super(accessToken, refreshToken);
		this.grantType = GrantType.BEARER;
	}
}
