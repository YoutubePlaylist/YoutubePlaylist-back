package com.example.youtubedb.config.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Value;

@Value
public class JwtConfig {
  String secretKey;
  SignatureAlgorithm signatureAlgorithm;
  String authoritiesKey = "auth";
  String bearerType = "bearer";

  public JwtConfig(String secretKey) {
    this.secretKey = secretKey;
    this.signatureAlgorithm = SignatureAlgorithm.HS512;
  }
}
