package com.example.youtubedb.testclient;

import com.example.youtubedb.dto.member.request.MemberLoginRequestDto;
import com.example.youtubedb.member.login.core.LoginRequest;
import com.example.youtubedb.member.Member2;
import com.example.youtubedb.member.login.core.MemberLogin2;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
class LoginController {

  private final MemberLogin2 memberLogin2;


  @PostMapping("/testlogin")
  public String testLogin(MemberLoginRequestDto dto){
    Member2 login = memberLogin2.login(JsonLoginRequestParser.parser(dto));

    return login.loginId();
    //MemberLogin2에서 리턴으로 entity 자체인 member2를 리턴해주는 것이 아니라 제공 할 값만 새로운 class로
    //뽑아내서 주는 것 추가

    //json요청에서 -> login 함수에 필요한 LoginRequest로 파싱하면서 login request를 public 으로
    //바꿔줘야했다.
    //하지만 그렇게 하지 않을 경우 의존성의 방향이 spring, 즉 클라언트에서 쓰일 controller 쪽에 의존되기
    //때문에 어쩔 수 없다 생각
    //방향 다시 생각해보자
  }

  static class JsonLoginRequestParser {
    static LoginRequest parser(MemberLoginRequestDto dto){
      return new LoginRequest(dto.getLoginId(), dto.getPassword());
    }
  }
}
