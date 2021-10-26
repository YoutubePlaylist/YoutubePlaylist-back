package com.example.youtubedb.member.login.core;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class BeanConfiguration {

  //다시 외부에서 의존한다.
  @Autowired
  private MyEncoder myEncoder;

  @Autowired
  private MemberRepository2 memberRepository2;

  @Bean
  public CheckPassword checkPassword() {
    return new CheckPassword(myEncoder);
  }

  @Bean
  MemberLogin2 memberLogin2() {
    return new MemberLogin2(memberRepository2, checkPassword());
  }
}
