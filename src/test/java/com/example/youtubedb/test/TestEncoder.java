package com.example.youtubedb.test;

public class TestEncoder implements MyEncoder{
  @Override
  public String encode(String rawPassword) {
    return rawPassword;
  }

  @Override
  public boolean matches(String rawPassword, String encodedPassword) {
    return rawPassword.equals(encodedPassword);
  }
}
