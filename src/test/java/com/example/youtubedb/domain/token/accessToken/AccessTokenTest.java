package com.example.youtubedb.domain.token.accessToken;

import com.example.youtubedb.config.jwt.time.ConstantTime;
import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import com.example.youtubedb.domain.token.accessToken.AccessToken;
import com.example.youtubedb.domain.token.accessToken.AccessTokenProvider;
import com.example.youtubedb.exception.ContractViolationException;
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

  @Test
  void AccessToken_loginID로_생성() {
    // given
    time = new ConstantTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));
    provider = new AccessTokenProvider(time);
    String loginId = "test001";
    AccessToken target = new AccessToken(
      loginId,
      time,
      time.now().truncatedTo(ChronoUnit.SECONDS).plus(ACCESS_TOKEN_EXPIRE_TIME));

    // when
    AccessToken accessToken = provider.create(loginId);

    // then
    assertThat(accessToken, is(target));
  }

  @Test
  void AccessToken_loginID와_만료기간으로_생성() {
    // given
    time = new ConstantTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));
    provider = new AccessTokenProvider(time);
    String loginId = "test001";
    Instant expirationAt = time.now().plus(ACCESS_TOKEN_EXPIRE_TIME);
    AccessToken target = new AccessToken(
      loginId,
      time,
      expirationAt);

    // when
    AccessToken accessToken = provider.create(loginId, expirationAt);

    // then
    assertThat(accessToken, is(target));
  }

  @Test
  void AccessToken의_만료시간은_과거X() {
    // given
    time = new ConstantTime(Instant.now().truncatedTo(ChronoUnit.SECONDS));
    provider = new AccessTokenProvider(time);
    String loginId = "test001";
    Instant expirationAt = time.now().minus(ACCESS_TOKEN_EXPIRE_TIME);

    // when & then
    assertThrows(ContractViolationException.class, () -> provider.create(loginId, expirationAt));
  }
}
