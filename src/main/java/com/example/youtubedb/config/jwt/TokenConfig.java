package com.example.youtubedb.config.jwt;

import com.example.youtubedb.repository.interfaces.MemberRepository;
import com.example.youtubedb.service.PlaylistService;
import com.example.youtubedb.service.member.MemberLogin;
import com.example.youtubedb.service.member.NonMemberRegister;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class TokenConfig {

  private final JwtConfigYamlAdapter jwtConfigYamlAdapter;

  @Bean
  public TokenProvider tokenProvider() {
    return new TokenProvider(jwtConfigYamlAdapter.toJwtConfig());
  }

}
