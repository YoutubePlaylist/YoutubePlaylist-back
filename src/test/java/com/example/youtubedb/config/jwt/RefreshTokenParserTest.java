package com.example.youtubedb.config.jwt;

import com.example.youtubedb.config.JwtSetConfig;
import com.example.youtubedb.domain.token.RefreshToken;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Period;
import java.time.temporal.ChronoUnit;

import static com.example.youtubedb.Fixture.curTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class RefreshTokenParserTest {
  String secretKey = "Dfdf23DSA23nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd32QtdHV0b3JpYWwK";
  JwtSetConfig jwtSetConfig = new JwtSetConfig(secretKey);
  RefreshToken.Provider tokenParser;
  RefreshTokenParser refreshTokenParser;

  @BeforeEach
  void setUp() {
    tokenParser = new RefreshToken.Provider(curTime());
    refreshTokenParser = new RefreshTokenParser(jwtSetConfig, tokenParser);
  }

  @Test
  void RefreshToken_파싱_테스트() {
    // given
    RefreshToken refreshToken = tokenParser.create(curTime().now().truncatedTo(ChronoUnit.SECONDS).plus(Period.ofDays(7)));
    JwtFormatter jwtFormatter = new JwtFormatter(jwtSetConfig);

    // when
    RefreshToken parsedRefreshToken = refreshTokenParser.parse(jwtFormatter.toJwtFromRefreshToken(refreshToken));

    // when
    assertThat(refreshToken, is(parsedRefreshToken));
  }
}