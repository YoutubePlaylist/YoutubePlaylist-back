package com.example.youtubedb.member.login.core;


public class TestEncoder implements MyEncoder {
  @Override
  public String encode(String rawPassword) {
    return rawPassword;
  }

  @Override
  public boolean matches(String rawPassword, String encodedPassword) {
    return rawPassword.equals(encodedPassword);
  }
}
