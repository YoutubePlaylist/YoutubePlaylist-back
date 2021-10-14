package com.example.youtubedb.test;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class CheckPassword {

  private final MyEncoder myEncoder;

  protected Boolean check(Member2 member, String password) {

    return myEncoder.matches(member.password(), password);
  }

}
