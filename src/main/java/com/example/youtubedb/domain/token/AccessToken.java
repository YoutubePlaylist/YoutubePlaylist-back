package com.example.youtubedb.domain.token;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static com.example.youtubedb.util.ContractUtil.requires;

@Value
@Accessors(fluent = true)
public class AccessToken {
  String loginId;
  Instant expirationAt;

  private AccessToken(
    String loginId,
    Instant expirationAt) {

    this.loginId = loginId;
    this.expirationAt = expirationAt;
  }

  @Component
  @RequiredArgsConstructor
  public static class Provider {
    private final Duration ACCESS_TOKEN_EXPIRE_TIME = Duration.ofMinutes(30);
    private final CurrentTimeServer currentTimeServer;

    public AccessToken create(String loginId) {
      requires(loginId != null && !loginId.isEmpty());

      return new AccessToken(loginId, currentTimeServer.now().truncatedTo(ChronoUnit.SECONDS).plus(ACCESS_TOKEN_EXPIRE_TIME));
    }

    public AccessToken create(String loginId, Instant expirationAt) {
      requires(loginId != null && !loginId.isEmpty());
      requires(expirationAt.isAfter(currentTimeServer.now()));

      return new AccessToken(loginId, expirationAt);
    }
  }

}
