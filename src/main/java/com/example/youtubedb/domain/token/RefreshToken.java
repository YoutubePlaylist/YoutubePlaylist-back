package com.example.youtubedb.domain.token;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.Instant;
import java.time.Period;
import java.time.temporal.ChronoUnit;

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
    private final Device pc;
    private final Device app;

    public Device device(boolean isPC) {
      if (isPC) {
        return pc;
      }
      return app;
    }
  }

  public interface Device {
    RefreshToken create();
  }

  @RequiredArgsConstructor
  public static class Pc implements Device {
    private final Period REFRESH_TOKEN_EXPIRE_DATE_PC = Period.ofDays(30);
    private final CurrentTimeServer currentTimeServer;

    @Override
    public RefreshToken create() {
      Instant expirationAt = currentTimeServer.now().truncatedTo(ChronoUnit.SECONDS).plus(REFRESH_TOKEN_EXPIRE_DATE_PC);

      return new RefreshToken(expirationAt);
    }
  }

  @RequiredArgsConstructor
  public static class App implements Device {
    private final Period REFRESH_TOKEN_EXPIRE_DATE_APP = Period.ofDays(7);
    private final CurrentTimeServer currentTimeServer;

    @Override
    public RefreshToken create() {
      Instant expirationAt = currentTimeServer.now().truncatedTo(ChronoUnit.SECONDS).plus(REFRESH_TOKEN_EXPIRE_DATE_APP);

      return new RefreshToken(expirationAt);
    }
  }

  @RequiredArgsConstructor
  public static class Provider {
    private final CurrentTimeServer currentTimeServer;

    public RefreshToken create(Instant expirationAt) {
      requires(expirationAt.isAfter(currentTimeServer.now()));

      return new RefreshToken(expirationAt);
    }
  }
}
