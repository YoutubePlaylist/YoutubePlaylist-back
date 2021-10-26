package com.example.youtubedb.member;

import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class Member2 {
  private final String loginId;
  private final String password;

  public Member2(String loginId, String password) {
    this.loginId = loginId;
    this.password = password;
  }
}