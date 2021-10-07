package com.example.youtubedb.config.jwt;

import com.example.youtubedb.aop.resolver.LoginUserArgumentResolver;
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

import javax.annotation.PostConstruct;

@Configuration
@RequiredArgsConstructor
public class TokenConfig {

  private final JwtConfigYamlAdapter jwtConfigYamlAdapter;
  private JwtConfig jwtConfig;

  @PostConstruct
  private void init(){
    jwtConfig = jwtConfigYamlAdapter.toJwtConfig();
  }

  @Bean
  public TokenProvider tokenProvider() {
    return new TokenProvider(jwtConfig);
  }

  @Bean
  public LoginUserArgumentResolver loginUserArgumentResolver() {
    return new LoginUserArgumentResolver(tokenProvider(), jwtConfig);
  }

}
