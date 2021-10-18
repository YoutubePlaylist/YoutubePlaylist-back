package com.example.youtubedb.domain.token;

import com.example.youtubedb.config.jwt.time.ConstantTime;
import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import com.example.youtubedb.exception.ContractViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;

import static com.example.youtubedb.Fixture.constTime;
import static com.example.youtubedb.Fixture.curTime;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RefreshTokenTest {
  final Period REFRESH_TOKEN_EXPIRE_DATE_APP = Period.ofDays(7);
  final Period REFRESH_TOKEN_EXPIRE_DATE_PC = Period.ofDays(30);
  RefreshToken.Provider refreshTokenProvider;
  CurrentTimeServer time;

  @BeforeEach
  void setUp() {
    time = new ConstantTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));
  }

  @Test
  void RefreshToken_PC() {
    refreshTokenProvider = new RefreshToken.Pc(time);
    RefreshToken refreshToken = refreshTokenProvider.create();
    assertThat(refreshToken.expirationAt(), is(time.now().plus(REFRESH_TOKEN_EXPIRE_DATE_PC)));
  }

  @Test
  void RefreshToken_APP() {
    refreshTokenProvider = new RefreshToken.App(time);

    RefreshToken refreshToken = refreshTokenProvider.create();
    assertThat(refreshToken.expirationAt(), is(time.now().plus(REFRESH_TOKEN_EXPIRE_DATE_APP)));
  }

  @Test
  void RefreshToken의_만료시간은_과거X() {
    RefreshToken.Parsing tokenParser = new RefreshToken.Parser(curTime());
    assertThrows(ContractViolationException.class, () -> tokenParser.parse(curTime().now().minus(Period.ofDays(1))));
  }
}
