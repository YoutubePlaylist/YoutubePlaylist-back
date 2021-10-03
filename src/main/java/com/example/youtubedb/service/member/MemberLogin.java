package com.example.youtubedb.service.member;

import com.example.youtubedb.config.jwt.TokenProvider;
import com.example.youtubedb.domain.Token;
import com.example.youtubedb.domain.member.Authority;
import com.example.youtubedb.domain.member.Member;
import com.example.youtubedb.dto.member.request.MemberLoginRequestDto;
import com.example.youtubedb.dto.member.request.NonMemberRequestDto;
import com.example.youtubedb.exception.DuplicateMemberException;
import com.example.youtubedb.exception.NotExistMemberException;
import com.example.youtubedb.repository.interfaces.MemberRepository;
import com.example.youtubedb.service.PlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public interface MemberLogin {
  Token login(MemberLoginRequestDto dto);

  @RequiredArgsConstructor
  class JwtMaker implements MemberLogin {

    private final MemberRepository memberRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @Override
    public Token login(MemberLoginRequestDto dto) {
      Member member = memberRepository.findByLoginId(dto.getLoginId()).orElseThrow(NotExistMemberException::new);

      AbstractAuthenticationToken authenticationToken = toAuthentication(
        dto.getLoginId(), member.isMember() ? dto.getPassword() : dto.getLoginId());

      Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
      return tokenProvider.generateTokenDto(authentication, dto.getIsPC());

    }
    
    private AbstractAuthenticationToken toAuthentication(String loginId, String password) {
      
      //TODO : 추상화 더해보기
      return new UsernamePasswordAuthenticationToken(loginId, password);
    }


  }

  @RequiredArgsConstructor
  class RedisJwtRegister implements MemberLogin {

    private final MemberLogin jwtMaker;
    private final ValueOperations<String, String> redisOperation;

    @Override
    public Token login(MemberLoginRequestDto dto) {
      Token token = jwtMaker.login(dto);
      setRedisRefreshToken(dto, token);
      return token;
    }
    private void setRedisRefreshToken(MemberLoginRequestDto dto, Token token){
      String key = dto.getIsPC() ? "PC" : "APP" + dto.getLoginId();
      redisOperation.set(key, token.getRefreshToken(),
        token.getRefreshTokenExpiresIn().getTime() -new Date().getTime(), TimeUnit.MILLISECONDS);
      return;
    }
  }
}
