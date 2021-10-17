package com.example.youtubedb.test;


import com.example.youtubedb.exception.DoNotMatchPasswordException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MemberLogin2Test {

  MemberMap repository = new MemberMap();
  MyEncoder encoder = new TestEncoder();
  MemberLogin2 memberLogin2 = new MemberLogin2(repository, encoder);

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
    assertThrows(DoNotMatchPasswordException.class, () -> memberLogin2.login("loginId", "password2"));
  }

  PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Test
  @DisplayName("비밀번호가 암호화 되어 있는 비밀번호를 잘 확인하니")
  void isEncryptedPassword() {
    //given
    Member2 member = new Member2("loginId", passwordEncoder.encode("password"));
    repository.save(member);
    repository.save(new Member2("loginId1", passwordEncoder.encode("password1")));
    repository.save(new Member2("loginId2", passwordEncoder.encode("password2")));

    //when
    Member2 loginMember = memberLogin2.login("loginId", "password");

    //then
    assertThat(repository.find(member.loginId()), is(loginMember));
    assertThat(repository.find(member.loginId()).password(), is(loginMember.password()));
  }

}