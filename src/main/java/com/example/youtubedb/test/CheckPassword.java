package com.example.youtubedb.test;

class CheckPassword {

  protected Boolean check(Member2 member, String password) {
    return member.password().equals(password);
  }

}
