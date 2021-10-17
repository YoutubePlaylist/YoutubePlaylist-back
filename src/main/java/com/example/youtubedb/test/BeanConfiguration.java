package com.example.youtubedb.test;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

  private final PasswordEncoder passwordEncoder;

  @Bean
  public MyEncoder myEncoder(){
    return new SpringEncoder(passwordEncoder);
  }

  @Bean
  MemberRepository2 memberRepository2() {
    return new MemberMap();
  }

  @Bean
  MemberLogin2 memberLogin2() {
    return new MemberLogin2(memberRepository2());
  }
}
