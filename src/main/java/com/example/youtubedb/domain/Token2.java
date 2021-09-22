package com.example.youtubedb.domain;

import static com.example.youtubedb.Contracts.requires;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.crypto.SecretKey;
import lombok.Value;
import lombok.experimental.Accessors;

@Value
@Accessors(fluent = true)
public class Token2 {
  String secretKey;
  String loginId;
  LocalDateTime expirationAt;
  SignatureAlgorithm signatureAlgorithm;

  public Token2(
    String secretKey,
    String loginId,
    LocalDateTime expirationAt,
    SignatureAlgorithm signatureAlgorithm) {

    requires(secretKey.length() > 0);
    requires(loginId.length() > 0);
    requires(expirationAt.isAfter(LocalDateTime.now()));
    requires(signatureAlgorithm != SignatureAlgorithm.NONE);

    this.secretKey = secretKey;
    this.loginId = loginId;
    this.expirationAt = expirationAt;
    this.signatureAlgorithm = signatureAlgorithm;
  }


  // 의존성 방향
}
