package com.example.youtubedb.config.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;

import static org.junit.jupiter.api.Assertions.*;

class JwtConfigYamlAdapterTest {
  @Value("$(jwt.secret)")
  String secretKey;

  @Test
  void name() {
    System.out.println(secretKey);
  }
}