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



// 의존성 방향이 중요하다!
// 추상화가 낮은게 높은걸 의존해야 한다: 의존성 방향 (DIP를 일반적으로 풀어낸말)
// 추상화 레벨 낮음 -> 추상화 레벨 높음
// 추상화 레벨?
// - 추상화 레벨이 높다: 입출력과거리가 멀다
// - 추상화 레벨이 낮다: 입출력과거리가 가깝다
// 입출력?
// - input / output
// - HTTP / console / file system / application.yaml / spring framework
//
