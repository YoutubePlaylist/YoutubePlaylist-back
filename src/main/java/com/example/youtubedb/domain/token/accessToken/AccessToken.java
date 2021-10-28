package com.example.youtubedb.domain.token.accessToken;

import com.example.youtubedb.config.jwt.time.CurrentTimeServer;
import lombok.Value;
import lombok.experimental.Accessors;

import java.time.Instant;

import static com.example.youtubedb.util.ContractUtil.requires;

@Value
@Accessors(fluent = true)
public class AccessToken {
  String loginId;
  Instant expirationAt;
  CurrentTimeServer currentTimeServer;

  AccessToken(
    String loginId,
    CurrentTimeServer currentTimeServer,
    Instant expirationAt) {
    requires(loginId != null && !loginId.isEmpty());
    requires(expirationAt.isAfter(currentTimeServer.now()));

    this.currentTimeServer = currentTimeServer;
    this.loginId = loginId;
    this.expirationAt = expirationAt;
  }
}
