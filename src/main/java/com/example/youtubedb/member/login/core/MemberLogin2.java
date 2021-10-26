package com.example.youtubedb.member.login.core;

import com.example.youtubedb.member.Member2;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberLogin2 {

  private final MemberRepository2 repository;
  private final CheckPassword checkPassword;

  public Member2 login(LoginRequest loginRequest) {
    Member2 member = repository.find(loginRequest.loginId());

    checkPassword.check(member, loginRequest.rawPassword());
    return member;
  }
}


