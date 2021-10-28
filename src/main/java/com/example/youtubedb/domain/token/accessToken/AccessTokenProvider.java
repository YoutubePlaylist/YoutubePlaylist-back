package com.example.youtubedb.domain.token.accessToken;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.RequiredArgsConstructor;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class AccessTokenProvider {
  private final CurrentTimeServer currentTimeServer;

  public AccessToken create(String loginId) {
    return new AccessToken(loginId, currentTimeServer, currentTimeServer.now().truncatedTo(ChronoUnit.SECONDS).plus(Duration.ofMinutes(30)));
  }

  public AccessToken create(String loginId, Instant expirationAt) {
    return new AccessToken(loginId, currentTimeServer, expirationAt);
  }
}
