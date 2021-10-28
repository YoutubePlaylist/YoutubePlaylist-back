package com.example.youtubedb.domain.token;

import com.example.youtubedb.config.jwt.time.ConstantTime;
import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import com.example.youtubedb.domain.token.accessToken.AccessToken;
import com.example.youtubedb.domain.token.accessToken.AccessTokenProvider;
import com.example.youtubedb.exception.ContractViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AccessTokenTest {
  private final Duration ACCESS_TOKEN_EXPIRE_TIME = Duration.ofMinutes(30);
  AccessTokenProvider provider;
  CurrentTimeServer time;

  @BeforeEach
  void setUp() {
    time = new ConstantTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));
    provider = new AccessTokenProvider(time);
  }

  @Test
  void AccessToken_loginID로_생성() {
    String loginId = "test001";
    AccessToken accessToken = provider.create(loginId);
    assertThat(accessToken.expirationAt(), is(time.now().plus(ACCESS_TOKEN_EXPIRE_TIME)));
  }

  @Test
  void AccessToken_loginID와_만료기간으로_생성() {
    String loginId = "test001";
    Instant expirationAt = time.now().plus(ACCESS_TOKEN_EXPIRE_TIME);

    AccessToken accessToken = provider.create(loginId, expirationAt);
    assertThat(accessToken.expirationAt(), is(time.now().plus(ACCESS_TOKEN_EXPIRE_TIME)));
  }

  @Test
  void AccessToken의_만료시간은_과거X() {
    String loginId = "test001";
    Instant expirationAt = time.now().minus(ACCESS_TOKEN_EXPIRE_TIME);

    assertThrows(ContractViolationException.class, () -> provider.create(loginId, expirationAt));
  }
}
