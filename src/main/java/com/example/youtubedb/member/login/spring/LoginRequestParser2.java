package com.example.youtubedb.member.login.spring;

import com.example.youtubedb.member.login.core.LoginRequest;
import com.example.youtubedb.member.login.spring.request.JsonNonMemberLoginRequest;
import com.example.youtubedb.member.login.spring.request.JsonRealMemberLoginRequest;

class LoginRequestParser2 {

  LoginRequest parse(JsonNonMemberLoginRequest dto) {
    return new LoginRequest(dto.deviceId(), dto.deviceId());
  }

  LoginRequest parse(JsonRealMemberLoginRequest dto) {
    return new LoginRequest(dto.loginId(), dto.password());
  }
}
