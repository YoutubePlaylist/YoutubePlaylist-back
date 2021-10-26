package com.example.youtubedb.member.login.core;

import com.example.youtubedb.member.Member2;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CheckPassword {

  private final MyEncoder myEncoder;

  protected Boolean check(Member2 member, String password) {

    return myEncoder.matches(member.password(), password);
  }

}
