package com.example.youtubedb.config.jwt;

import com.example.youtubedb.domain.AccessToken;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.sql.Timestamp;
import javax.crypto.SecretKey;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public final class JwtFormatter {
  private final JwtConfig jwtConfig;

  public String toJwt(AccessToken accessToken) {
    final SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.secretKey()));

    return Jwts.builder()
      .setSubject(accessToken.loginId())
      .setExpiration(Timestamp.valueOf(accessToken.expirationAt()))
      .signWith(key, jwtConfig.signatureAlgorithm())
      .compact();
  }
}
