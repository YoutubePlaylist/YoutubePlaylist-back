package com.example.youtubedb.test;

import com.example.youtubedb.exception.DoNotMatchPasswordException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberLogin2 {

  private final MemberRepository2 repository;
  private final CheckPassword checkPassword;

  public Member2 login(String loginId, String password) {
    Member2 member = repository.find(loginId);

    checkPassword.check(member, password);
    return member;
  }

}
