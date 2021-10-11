package com.example.youtubedb.test;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MemberLogin2 {

  private final MemberRepository2 repository;

  public Member2 login(String loginId, String password){
    return repository.find(loginId);
  }
}
