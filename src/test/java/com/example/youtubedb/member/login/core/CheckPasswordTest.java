package com.example.youtubedb.member.login.core;

import com.example.youtubedb.member.Member2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class CheckPasswordTest {

  TestEncoder testEncoder = new TestEncoder();
  CheckPassword checkPassword = new CheckPassword(testEncoder);

  @Test
  @DisplayName("비밀번호를 맞게 비교 하는가?")
  void isCorrectPassword(){
    //given
    Member2 member = new Member2("loginId", testEncoder.encode("password123"));

    //then
    assertThat(checkPassword.check(member, "password123"), is(true));
    assertThat(checkPassword.check(member, "password"), is(false));
  }
}