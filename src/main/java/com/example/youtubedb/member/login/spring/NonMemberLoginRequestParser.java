package com.example.youtubedb.member.login.spring;

import com.example.youtubedb.member.login.core.LoginRequest;
import com.example.youtubedb.member.login.spring.request.JsonNonMemberLoginRequest;


class NonMemberLoginRequestParser implements LoginRequestParser<JsonNonMemberLoginRequest> {

  @Override
  public LoginRequest parse(JsonNonMemberLoginRequest dto) {
    return new LoginRequest(dto.deviceId(), dto.deviceId());
  }
}