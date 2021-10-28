package com.example.youtubedb.member.login.spring;

import com.example.youtubedb.member.login.core.LoginRequest;
import com.example.youtubedb.member.login.spring.request.JsonNonMemberLoginRequest;
import com.example.youtubedb.member.login.spring.request.JsonRealMemberLoginRequest;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class LoginRequestParserTest {

  LoginRequestParser<JsonNonMemberLoginRequest> nonMemberParser = new NonMemberLoginRequestParser();
  LoginRequestParser<JsonRealMemberLoginRequest> memberParser = new RealMemberLoginRequestParser();

  @Test
  void 회원_LoginRequest_parsing() {
    JsonRealMemberLoginRequest request = new JsonRealMemberLoginRequest("loginId", "password12");
    LoginRequest loginRequest = memberParser.parse(request);

    assertThat(loginRequest, is(new LoginRequest("loginId", "password12")));

  }

  @Test
  void 비회원_LoginRequest_parsing() {
    String deviceId = "deviceIdQWERASDF";
    JsonNonMemberLoginRequest request = new JsonNonMemberLoginRequest(deviceId);
    LoginRequest loginRequest = nonMemberParser.parse(request);

    assertThat(loginRequest, is(new LoginRequest(deviceId, deviceId)));

  }

}