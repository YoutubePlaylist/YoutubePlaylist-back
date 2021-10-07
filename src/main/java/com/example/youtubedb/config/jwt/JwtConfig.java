package com.example.youtubedb.config.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Value;

@Value
public class JwtConfig {
  String secretKey;
  SignatureAlgorithm signatureAlgorithm;

  public JwtConfig(String secretKey) {
    this.secretKey = secretKey;
    this.signatureAlgorithm = SignatureAlgorithm.HS512;
  }
}
