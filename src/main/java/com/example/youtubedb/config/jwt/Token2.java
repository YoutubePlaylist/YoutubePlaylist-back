package com.example.youtubedb.config.jwt;

import static com.example.youtubedb.Contracts.requires;

import io.jsonwebtoken.SignatureAlgorithm;
import java.time.LocalDateTime;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class Token2 {
  String loginId;
  LocalDateTime expirationAt;
  SignatureAlgorithm signatureAlgorithm;

  public Token2(String loginId, LocalDateTime expirationAt, SignatureAlgorithm signatureAlgorithm) {
    requires(loginId.length() > 0);
    requires(expirationAt.isAfter(LocalDateTime.now()));
    requires(signatureAlgorithm != SignatureAlgorithm.NONE);

    this.loginId = loginId;
    this.expirationAt = expirationAt;
    this.signatureAlgorithm = signatureAlgorithm;
  }
}
