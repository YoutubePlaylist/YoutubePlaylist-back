package com.example.youtubedb.config.jwt;

import com.example.youtubedb.domain.Token2;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.sql.Timestamp;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class JwtFormatter {
  private final JwtConfig jwtConfig;

  public String toJwt(Token2 token2) {
    final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.secretKey()));

    return Jwts.builder()
      .setSubject(token2.loginId())
      .setExpiration(Timestamp.valueOf(token2.expirationAt()))
      .signWith(key, jwtConfig.signatureAlgorithm())
      .compact();
  }
}
