package com.example.youtubedb.member.login.spring.request;


import lombok.Getter;
import lombok.experimental.Accessors;

@Getter
@Accessors(fluent = true)
public class JsonRealMemberLoginRequest {
  private final String loginId;
  private final String password;

  public JsonRealMemberLoginRequest(String loginId, String password) {
    this.loginId = loginId;
    this.password = password;
  }
}

