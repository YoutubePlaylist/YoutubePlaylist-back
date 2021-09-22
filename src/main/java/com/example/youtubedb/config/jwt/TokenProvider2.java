package com.example.youtubedb.config.jwt;

import static java.sql.Timestamp.valueOf;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import javax.crypto.SecretKey;

public class TokenProvider2 {

  public Token2 create() {
    final byte[] keyBytes = Decoders.BASE64.decode("c3ByaW5nLWJvb3Qtc2VjdXJpdHktand0LXR1dG9yaWFsLWppd29vbi1zcHJpbmctYm9vdC1zZWN1cml0eS1qd3QtdHV0b3JpYWwK");
    final SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);

    final String compact = Jwts.builder()
      .setSubject("id-123")       // payload "sub": "name"
      .setExpiration(valueOf(
        LocalDateTime.now().plus(Duration.ofMinutes(30))))      // payload "exp": 1516239022 (예시)
      .signWith(secretKey, SignatureAlgorithm.HS512)    // header "alg": "HS512"
      .compact();

    return null;
  }

  public static class Token2 {

    public String loginId() {
      return null;
    }

    public LocalDateTime expirationAt() {
      return null;
    }

    public SignatureAlgorithm signatureAlgorithm() {
      return null;
    }
  }
}
