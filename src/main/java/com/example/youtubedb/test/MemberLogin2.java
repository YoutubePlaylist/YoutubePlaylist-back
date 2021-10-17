package com.example.youtubedb.test;

import com.example.youtubedb.exception.DoNotMatchPasswordException;
import lombok.RequiredArgsConstructor;

import javax.annotation.PostConstruct;

@RequiredArgsConstructor
public class MemberLogin2 {

  private final MemberRepository2 repository;
  private final MyEncoder myEncoder;

  @PostConstruct
  void test1(){
    repository.save(new Member2("testId", myEncoder.encode("password")));
  }

  public Member2 login(String loginId, String password){
    Member2 member = repository.find(loginId);

    checkPassword(member, password);
    return member;
  }

  private void checkPassword(Member2 member, String password) {
    if(!myEncoder.matches(password, member.password())){
      throw new DoNotMatchPasswordException();
    }
  }
}
