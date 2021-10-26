package com.example.youtubedb.member.login.spring;

import com.example.youtubedb.dto.member.request.MemberLoginRequestDto;
import com.example.youtubedb.member.login.core.LoginRequest;

public class JsonLoginRequestParser {
  LoginRequest parser(MemberLoginRequestDto dto){
    return new LoginRequest(dto.getLoginId(), dto.getPassword());
  }
}
