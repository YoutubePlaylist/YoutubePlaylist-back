package com.example.youtubedb.domain.token.refreshToken;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.Instant;

import static com.example.youtubedb.util.ContractUtil.requires;

@Value
@Accessors(fluent = true)
public class RefreshToken {
  Instant expirationAt;
  CurrentTimeServer currentTimeServer;

  RefreshToken(Instant expirationAt, CurrentTimeServer currentTimeServer) {
    requires(expirationAt.isAfter(currentTimeServer.now()));

    this.currentTimeServer = currentTimeServer;
    this.expirationAt = expirationAt;
  }
}
