package com.example.youtubedb.test;

import com.example.youtubedb.exception.DoNotMatchPasswordException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

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

@AllArgsConstructor
@Getter
@Accessors(fluent = true)
class LoginRequest {
  private String loginId;
  private final String rawPassword;
}
