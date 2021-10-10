package com.example.youtubedb.test;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class MemberLogin2Test {

  MemberRepository2 repository = new MemberRepository2();

  @Test()
  @DisplayName("비밀번호를_맞게_입력해야니")
  void isThePasswordCorrect() {
    Member2 member = new Member2("loginId", "password");
    repository.save(member);

    assertThat(repository.find(member.loginId()), is(member));
    assertThat(repository.find(member.loginId()).password(), is(member.password()));
  }
}