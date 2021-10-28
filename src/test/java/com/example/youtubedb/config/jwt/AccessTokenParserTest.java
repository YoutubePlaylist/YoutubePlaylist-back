package com.example.youtubedb.config.jwt;

import com.example.youtubedb.Fixture;
import com.example.youtubedb.config.JwtSetConfig;
import com.example.youtubedb.domain.token.accessToken.AccessToken;
import com.example.youtubedb.domain.token.accessToken.AccessTokenProvider;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class AccessTokenParserTest {
	String secretKey = "Dfdf23DSA23nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd32QtdHV0b3JpYWwK";
	JwtSetConfig jwtSetConfig = new JwtSetConfig(secretKey);
	AccessTokenProvider provider = new AccessTokenProvider(Fixture.curTime());
	AccessTokenParser accessTokenParser = new AccessTokenParser(jwtSetConfig, provider);

	@Test
	void AccessToken_파싱_테스트() {
		String loginId = "testMan";
		AccessToken accessToken = provider.create(loginId);
		JwtFormatter jwtFormatter = new JwtFormatter(jwtSetConfig);

		// when
		AccessToken parsedAccessToken = accessTokenParser.parse(jwtFormatter.toJwtFromAccessToken(accessToken));

		// when
		assertThat(parsedAccessToken, is(accessToken));
	}
}