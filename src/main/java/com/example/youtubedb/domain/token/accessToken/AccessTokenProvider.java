package com.example.youtubedb.domain.token.accessToken;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.RequiredArgsConstructor;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import static com.example.youtubedb.util.ContractUtil.requires;

@RequiredArgsConstructor
public class AccessTokenProvider {
  private final CurrentTimeServer currentTimeServer;

  public AccessToken create(String loginId) {
    requires(loginId != null && !loginId.isEmpty());

    return new AccessToken(loginId, currentTimeServer.now().truncatedTo(ChronoUnit.SECONDS).plus(Duration.ofMinutes(30)));
  }

  public AccessToken create(String loginId, Instant expirationAt) {
    requires(loginId != null && !loginId.isEmpty());
    requires(expirationAt.isAfter(currentTimeServer.now()));

    return new AccessToken(loginId, expirationAt);
  }
}
