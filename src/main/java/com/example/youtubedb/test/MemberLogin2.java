package com.example.youtubedb.test;

import com.example.youtubedb.exception.DoNotMatchPasswordException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberLogin2 {

  private final MemberRepository2 repository;

  public Member2 login(String loginId, String password){
    Member2 member = repository.find(loginId);

    checkPassword(member, password);
    return member;
  }

  private void checkPassword(Member2 member, String password) {
    if(!member.password().equals(password)){
      throw new DoNotMatchPasswordException();
    }
  }
}
