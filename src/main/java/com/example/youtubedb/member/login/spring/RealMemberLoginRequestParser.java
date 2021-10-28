package com.example.youtubedb.member.login.spring;

import com.example.youtubedb.member.login.core.LoginRequest;
import com.example.youtubedb.member.login.spring.request.JsonRealMemberLoginRequest;


class RealMemberLoginRequestParser implements LoginRequestParser<JsonRealMemberLoginRequest> {


  @Override
  public LoginRequest parse(JsonRealMemberLoginRequest dto) {
    return new LoginRequest(dto.loginId(), dto.password());
  }
}