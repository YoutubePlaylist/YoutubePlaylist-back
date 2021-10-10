package com.example.youtubedb.test;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;

class MemberLogin2Test {

  MemberRepository2 repository;

  @Test()
  @DisplayName("비밀번호를_맞게_입력해야니")
  void isThePasswordCorrect() {
    Member2 member = new Member2("loginId", "password");
    repository.save(member);

    assertThat(repository.find(member.id()), is(member));
    assertThat(repository.find(member.id()).password(), is(member.password()));
  }
}