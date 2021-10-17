package com.example.youtubedb.test;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Bean
  public MyEncoder myEncoder() {
    return new SpringEncoder(passwordEncoder);
  }

  @Bean
  public CheckPassword checkPassword() {
    return new CheckPassword(myEncoder());
  }

  @Bean
  MemberRepository2 memberRepository2() {
    return new MemberMap();
  }

  @Bean
  MemberLogin2 memberLogin2() {
    return new MemberLogin2(memberRepository2(), checkPassword());
  }
}
