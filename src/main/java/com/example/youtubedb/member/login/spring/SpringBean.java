package com.example.youtubedb.member.login.spring;

import com.example.youtubedb.member.login.core.MemberRepository2;
import com.example.youtubedb.member.login.core.MyEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SpringBean {
  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Bean
  public MyEncoder myEncoder() {
    return new SpringEncoder(passwordEncoder);
  }
  @Bean
  MemberRepository2 memberRepository2() {
    return new MemberMap();
    /*
    return JpaMember2Repository2();
     */
  }
}
