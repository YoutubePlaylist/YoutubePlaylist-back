package com.example.youtubedb.domain.token;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;

import static com.example.youtubedb.util.ContractUtil.requires;

@Value
@Accessors(fluent = true)
public class RefreshToken {
  Instant expirationAt;

  private RefreshToken(Instant expirationAt) {
    requires(expirationAt.isAfter(Instant.now()));
    this.expirationAt = expirationAt;
  }

  @RequiredArgsConstructor
  public static class Mapping {
    private final Provider pc;
    private final Provider app;

    public Provider provider(boolean isPC) {
      if (isPC) {
        return pc;
      }
      return app;
    }
  }

  public interface Provider {
    RefreshToken create();
  }

  @RequiredArgsConstructor
  public static class Pc implements Provider {
    private final Period REFRESH_TOKEN_EXPIRE_DATE_PC = Period.ofDays(30);
    private final CurrentTimeServer currentTimeServer;

    @Override
    public RefreshToken create() {
      return new RefreshToken(
        currentTimeServer.now().truncatedTo(ChronoUnit.SECONDS).plus(REFRESH_TOKEN_EXPIRE_DATE_PC));
    }
  }

  @RequiredArgsConstructor
  public static class App implements Provider {
    private final Period REFRESH_TOKEN_EXPIRE_DATE_APP = Period.ofDays(7);
    private final CurrentTimeServer currentTimeServer;

    @Override
    public RefreshToken create() {
      return new RefreshToken(
        currentTimeServer.now().truncatedTo(ChronoUnit.SECONDS).plus(REFRESH_TOKEN_EXPIRE_DATE_APP));
    }
  }
}
