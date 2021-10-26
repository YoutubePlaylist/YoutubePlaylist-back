package com.example.youtubedb.member.login.core;

public interface MyEncoder {
  String encode(String rawPassword);
  boolean matches(String rawPassword, String encodedPassword);
}
