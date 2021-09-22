package com.example.youtubedb.domain;

import static com.example.youtubedb.Contracts.requires;

import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class AccessToken {
  String loginId;
  LocalDateTime expirationAt;
  SignatureAlgorithm signatureAlgorithm;

  public AccessToken(
    String secretKey,
    String loginId,
    LocalDateTime expirationAt,
    SignatureAlgorithm signatureAlgorithm) {

    requires(secretKey.length() > 0);
    requires(loginId.length() > 0);
    requires(expirationAt.isAfter(LocalDateTime.now()));
    requires(signatureAlgorithm != SignatureAlgorithm.NONE);

    this.loginId = loginId;
    this.expirationAt = expirationAt;
    this.signatureAlgorithm = signatureAlgorithm;
  }
}
