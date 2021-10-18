package com.example.youtubedb.config.jwt;

import com.example.youtubedb.domain.token.AccessToken;
import com.example.youtubedb.domain.token.Jwt;
import com.example.youtubedb.domain.token.RefreshToken;
import com.example.youtubedb.domain.token.Token;
import com.example.youtubedb.dto.member.MemberForTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import static java.sql.Timestamp.valueOf;

@Slf4j
@Component
@RequiredArgsConstructor
public class TokenProvider {
  private final AccessTokenProvider accessTokenProvider;
  private final RefreshToken.Mapping refreshTokenMapper;
  private final RefreshTokenParser refreshTokenParser;


  public Token create(MemberForTokenDto memberForTokenDto) {
    // Access Token 생성
    AccessToken accessToken = accessTokenProvider.create(memberForTokenDto.getLoginId());

    // Refresh Token 생성
    RefreshToken refreshToken = refreshTokenMapper.provider(memberForTokenDto.isPc()).create();

    return Jwt.builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .build();
  }

  public Token reissue(String loginId, String refreshTokenValue) {
    AccessToken accessToken = accessTokenProvider.create(loginId);
    RefreshToken refreshToken = refreshTokenParser.parse(refreshTokenValue);
    return Jwt.builder()
      .accessToken(accessToken)
      .refreshToken(refreshToken)
      .build();
  }
}