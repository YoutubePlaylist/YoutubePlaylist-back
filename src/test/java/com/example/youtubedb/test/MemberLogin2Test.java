package com.example.youtubedb.test;


import com.example.youtubedb.exception.DoNotMatchPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberLogin2Test {

  MemberRepository2 repository = new MemberRepository2();
  MemberLogin2 memberLogin2 = new MemberLogin2(repository);

  @Test
  @DisplayName("login ID를 통해 올바른 Member를 가져오니")
  void isTheCorrectMember() {
    //given
    Member2 member = new Member2("loginId", "password");
    repository.save(member);

    //when
    Member2 loginMember = memberLogin2.login("loginId", "password");

    //then
    assertThat(repository.find(member.loginId()), is(loginMember));
  }

  @Test
  @DisplayName("login ID를 통해 올바른 Member를 가져오니 - 여러 명일 경우")
  void isTheCorrectMember2() {
    //given
    Member2 member = new Member2("loginId", "password");
    repository.save(member);
    repository.save(new Member2("loginId1", "password1"));
    repository.save(new Member2("loginId2", "password2"));

    //when
    Member2 loginMember = memberLogin2.login("loginId", "password");

    //then
    assertThat(repository.find(member.loginId()), is(loginMember));
  }


  @Test
  @DisplayName("비밀번호는 잘 비교하니")
  void isCorrectPassword() {
    //given
    Member2 member = new Member2("loginId", "password");
    repository.save(member);
    repository.save(new Member2("loginId1", "password1"));
    repository.save(new Member2("loginId2", "password2"));

    //when
    Member2 loginCorrectPassword = memberLogin2.login("loginId", "password");

    //then
    assertThat(repository.find(member.loginId()), is(loginCorrectPassword));
  }

  @Test
  @DisplayName("비밀번호는 잘 비교하니 - 틀린 경우")
  void isCorrectPassword2() {
    //given
    Member2 member = new Member2("loginId", "password");
    repository.save(member);
    repository.save(new Member2("loginId1", "password1"));
    repository.save(new Member2("loginId2", "password2"));

    //then
    assertThrows(DoNotMatchPasswordException.class, () -> memberLogin2.login("loginId", "password2"));
  }
}