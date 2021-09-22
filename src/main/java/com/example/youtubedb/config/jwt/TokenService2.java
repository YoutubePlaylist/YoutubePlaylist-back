package com.example.youtubedb.config.jwt;

import com.example.youtubedb.domain.AccessToken;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TokenService2 {
  private final CurrentTimeServer currentTimeServer;
  private final JwtConfig jwtConfig;
  private final JwtParser parser;



  public TokenService2(CurrentTimeServer currentTimeServer,
    JwtConfig jwtConfig) {
    this.currentTimeServer = currentTimeServer;
    this.jwtConfig = jwtConfig;
    this.parser = Jwts.parserBuilder()
      .setSigningKey(jwtConfig.secretKey())
      .build();
  }

  public AccessToken create(String loginId) {
    return new AccessToken(
      jwtConfig.secretKey(),
      loginId,
      currentTimeServer.now().plus(Duration.ofMinutes(30)),
      jwtConfig.signatureAlgorithm());
  }


  // 짚고 넘어감!
  public AccessToken parseToken(String token) {
    final Jws<Claims> claimsJws = parser.parseClaimsJws(token);
    final Claims claims = claimsJws.getBody();
    final String loginId = claims.getSubject();
    final LocalDateTime expiration = claims
      .getExpiration()
      .toInstant()
      .atZone(ZoneId.systemDefault())
      .toLocalDateTime();

    final SignatureAgorithm signatureAlgorithm = SignatureAlgorithm.forName(claimsJws.getHeader().getAlgorithm());
    return new AccessToken(jwtConfig.secretKey(), loginId, expiration, signatureAlgorithm);
  }

  // Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJpZC00NTYiLCJleHAiOjE2MzIyOTI0OTR9.r79sTygbR_kGYIqglkD4A2i3nwVD5L1PoORKpq-bGTUPT5IPZZOo4dDP0fffaP7c93M48JWOAEP0VsXnF8Ze4g
}
