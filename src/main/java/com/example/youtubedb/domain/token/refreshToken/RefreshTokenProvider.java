package com.example.youtubedb.domain.token.refreshToken;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
public class RefreshTokenProvider {
  private final CurrentTimeServer currentTimeServer;

  public RefreshToken create(Instant expirationAt) {
    return new RefreshToken(expirationAt, currentTimeServer);
  }
}
