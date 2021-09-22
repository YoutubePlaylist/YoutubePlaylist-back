package com.example.youtubedb.config.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigYamlAdapter {
  @Value("${spring.jwt.secretKey}")
  String secretKey;
  @Value("${spring.jwt.alg}")
  SignatureAlgorithm signatureAlgorithm;

  public JwtConfig toJwtConfig() {
    return new JwtConfig(secretKey);
  }
}
