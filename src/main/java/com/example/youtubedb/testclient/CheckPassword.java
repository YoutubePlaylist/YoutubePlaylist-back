package com.example.youtubedb.testclient;

class CheckPassword {

  private final MyEncoder myEncoder = new MyEncoder();

  protected Boolean check(String savedPassword, String inputPassword) {

    return myEncoder.matches(savedPassword, inputPassword);
  }

}
