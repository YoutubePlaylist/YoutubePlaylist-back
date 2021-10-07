package com.example.youtubedb.config.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JwtConfigYamlAdapter {
  @Value("${jwt.secret}")
  String secretKey;

  public JwtConfig toJwtConfig() {
    return new JwtConfig(secretKey);
  }
}
