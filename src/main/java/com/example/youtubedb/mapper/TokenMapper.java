package com.example.youtubedb.mapper;

import com.example.youtubedb.config.jwt.JwtFormatter;
import com.example.youtubedb.domain.token.Token;
import com.example.youtubedb.vo.TokenVO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TokenMapper {
  final JwtFormatter jwtFormatter;

  public TokenVO toTokenVO(Token token) {
    String accessTokenValue = jwtFormatter.toJwtFromAccessToken(token.getAccessToken());
    String refreshTokenValue = jwtFormatter.toJwtFromRefreshToken(token.getRefreshToken());
    return new TokenVO(accessTokenValue, refreshTokenValue);
  }
}
