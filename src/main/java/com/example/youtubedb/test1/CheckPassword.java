package com.example.youtubedb.test1;

import com.example.youtubedb.test.Member2;
import lombok.RequiredArgsConstructor;

class CheckPassword {

  private final MyEncoder myEncoder = new MyEncoder();

  protected Boolean check(String savedPassword, String inputPassword) {

    return myEncoder.matches(savedPassword, inputPassword);
  }

}
