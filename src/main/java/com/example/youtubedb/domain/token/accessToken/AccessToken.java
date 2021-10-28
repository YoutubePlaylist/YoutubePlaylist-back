package com.example.youtubedb.domain.token.accessToken;

import lombok.Value;
import lombok.experimental.Accessors;

import java.time.Instant;

@Value
@Accessors(fluent = true)
public class AccessToken {
  String loginId;
  Instant expirationAt;

  AccessToken(
    String loginId,
    Instant expirationAt) {

    this.loginId = loginId;
    this.expirationAt = expirationAt;
  }
}
