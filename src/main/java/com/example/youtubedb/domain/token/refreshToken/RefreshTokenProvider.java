package com.example.youtubedb.domain.token.refreshToken;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.RequiredArgsConstructor;
import java.time.Instant;
import static com.example.youtubedb.util.ContractUtil.requires;

@RequiredArgsConstructor
public class RefreshTokenProvider {
  private final CurrentTimeServer currentTimeServer;

  public RefreshToken create(Instant expirationAt) {
    requires(expirationAt.isAfter(currentTimeServer.now()));

    return new RefreshToken(expirationAt);
  }
}
