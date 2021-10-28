package com.example.youtubedb.domain.token.refreshToken;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
public class PC implements Device {
  private final CurrentTimeServer currentTimeServer;

  @Override
  public RefreshToken create() {
    Instant expirationAt = currentTimeServer.now().truncatedTo(ChronoUnit.SECONDS).plus(Period.ofDays(30));

    return new RefreshToken(expirationAt, currentTimeServer);
  }
}
