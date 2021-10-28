package com.example.youtubedb.config.jwt;

import com.example.youtubedb.config.JwtSetConfig;
import com.example.youtubedb.domain.token.refreshToken.RefreshToken;
import com.example.youtubedb.domain.token.refreshToken.RefreshTokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;

import java.time.Instant;

public class RefreshTokenParser implements TokenParser<RefreshToken> {
  private final JwtParser parser;
  private final RefreshTokenProvider provider;

  public RefreshTokenParser(JwtSetConfig jwtSetConfig, RefreshTokenProvider tokeParser) {
    this.parser = Jwts.parserBuilder()
      .setSigningKey(jwtSetConfig.secretKey())
      .build();
    this.provider = tokeParser;
  }

  @Override
  public RefreshToken parse(String tokenString) {
    final Jws<Claims> claimsJws = parser.parseClaimsJws(tokenString);
    final Claims claims = claimsJws.getBody();
    final Instant expiration = claims
      .getExpiration()
      .toInstant();

    return provider.create(expiration);
  }
}
